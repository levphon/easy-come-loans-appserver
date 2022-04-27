package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.entity.OcrIdcard;
import cn.com.payu.app.modules.entity.SensetimeLiveness;
import cn.com.payu.app.modules.mapper.OcrIdcardMapper;
import cn.com.payu.app.modules.mapper.SensetimeLivenessMapper;
import cn.com.payu.app.modules.model.sensetime.req.LivenessAuthTokenReq;
import cn.com.payu.app.modules.model.sensetime.req.LivenessDetectionResultReq;
import cn.com.payu.app.modules.model.sensetime.req.OcrIdcardStatelessReq;
import cn.com.payu.app.modules.model.sensetime.resp.LivenessAuthTokenResp;
import cn.com.payu.app.modules.model.sensetime.resp.LivenessDetectionResultResp;
import cn.com.payu.app.modules.model.sensetime.resp.OcrIdcardStatelessInfo;
import cn.com.payu.app.modules.model.sensetime.resp.OcrIdcardStatelessResp;
import cn.com.payu.app.modules.utils.AppContextHolder;
import cn.com.payu.app.modules.utils.Base64Utils;
import cn.com.payu.app.modules.utils.SenseTimeUtils;
import com.glsx.plat.common.utils.HttpUtils;
import com.glsx.plat.common.utils.SnowFlake;
import com.glsx.plat.common.utils.StringUtils;
import com.glsx.plat.context.properties.UploadProperties;
import com.glsx.plat.context.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class SenseTimeService {

//    https://v2-auth-api.visioncloudapi.com/ocr/idcard/stateless
//    https://v2-auth-api.visioncloudapi.com/v2/h5/liveness/auth_token
//    https://v2-auth-api.visioncloudapi.com/v2/h5/liveness/detection_result

    @Autowired
    private SenseTimeUtils senseTimeUtils;

    @Autowired
    private UploadProperties uploadProperties;

    @Resource
    private OcrIdcardMapper ocrIdcardMapper;

    @Resource
    private SensetimeLivenessMapper sensetimeLivenessMapper;

    public OcrIdcardStatelessResp ocrIdcardStateless(OcrIdcardStatelessReq req, String url, Long userId) {
        OcrIdcardStatelessResp resp = senseTimeUtils.accessByPost("/ocr/idcard/stateless", req, OcrIdcardStatelessResp.class);
        if ("1000".equals(resp.getCode())) {
            OcrIdcardStatelessInfo statelessInfo = resp.getInfo();
            if (statelessInfo != null) {
                OcrIdcard idcard = ocrIdcardMapper.selectByUserId(userId);
                if (idcard == null) {
                    idcard = new OcrIdcard();
                }
                idcard.setUserId(userId);
                if ("front".equals(resp.getSide())) {
                    idcard.setFrontUrl(url);
                    idcard.setCid(statelessInfo.getNumber());
                    idcard.setName(statelessInfo.getName());
                    idcard.setGender(statelessInfo.getGender());
                    idcard.setAddress(statelessInfo.getAddress());
                    idcard.setNation(statelessInfo.getNation());
                    idcard.setBirthday(statelessInfo.getYear() + "-" + statelessInfo.getMonth() + "-" + statelessInfo.getDay());
                } else if ("back".equals(resp.getSide())) {
                    idcard.setBackUrl(url);
                    idcard.setIssuedBy(statelessInfo.getAuthority());
                    idcard.setValidDate(statelessInfo.getTimeLimit());
                }
                ocrIdcardMapper.saveOrUpdate(idcard);
            }
        }
        return resp;
    }

    public LivenessAuthTokenResp livenessAuthToken() {
        LivenessAuthTokenReq req = new LivenessAuthTokenReq();

        String bizNo = SnowFlake.nextSerialNumber();
        req.setBizNo(bizNo);
        req.setRedirectUrl(PropertiesUtils.getProperty("liveness.h5.redirect_url") + bizNo);
        req.setIsShowDefaultResultPage(false);
        req.setLivenessMode("interactive");
        req.setReturnFaceImage(true);
        req.setReturnImage(true);

        LivenessAuthTokenResp resp = senseTimeUtils.accessByPost("/v2/h5/liveness/auth_token", req, LivenessAuthTokenResp.class);
        if (!"1000".equals(resp.getCode())) {

        }

        SensetimeLiveness liveness = new SensetimeLiveness();
        liveness.setCuserId(AppContextHolder.getUserId());
        liveness.setBizNo(bizNo);
        liveness.setBizToken(resp.getBizToken());
        liveness.setStatus(0);
        liveness.setCreatedDate(new Date());
        sensetimeLivenessMapper.insert(liveness);

        return resp;
    }

    public LivenessDetectionResultResp livenessDetectionResult(String bizNo) {

        SensetimeLiveness liveness = sensetimeLivenessMapper.selectByBizNo(bizNo);

        if (liveness != null) {
            LivenessDetectionResultReq resultReq = new LivenessDetectionResultReq();
            resultReq.setBiztoken(liveness.getBizToken());

            LivenessDetectionResultResp resp = senseTimeUtils.accessByGet("/v2/h5/liveness/detection_result", resultReq, LivenessDetectionResultResp.class);
            if ("1000".equals(resp.getCode())) {
                //获取文件存放路径
                String absolutePath = uploadProperties.getBasePath();
                String path = PropertiesUtils.getProperty("app.img.url") + uploadProperties.getFilepath();

                String livenessImage = liveness.getBizToken() + "_1.png";
                String livenessFaceImage = liveness.getBizToken() + "_2.png";

                Base64Utils.base64ToImage(resp.getBase64LivenessImage(), absolutePath + livenessImage);
                Base64Utils.base64ToImage(resp.getBase64LivenessFaceImage(), absolutePath + livenessFaceImage);

                liveness.setLivenessImage(path + livenessImage);
                liveness.setLivenessFaceImage(path + livenessFaceImage);
                liveness.setStatus(resp.getPassed() != null && resp.getPassed() ? 1 : 2);
                liveness.setUpdatedDate(new Date());
                sensetimeLivenessMapper.updateByPrimaryKeySelective(liveness);
            } else {

            }
            return resp;
        }
        return null;
    }

    public String getLivenessImage(String url) {
        if (StringUtils.isNotEmpty(url)) {
            String filename = AppContextHolder.getUserId() + "_liveness.png";

            //获取文件存放路径
            String absolutePath = uploadProperties.getBasePath();
            HttpUtils.downloadFile(url, absolutePath + filename);

            //网络访问路径
            String path = PropertiesUtils.getProperty("app.img.url") + uploadProperties.getFilepath();

            SensetimeLiveness liveness = sensetimeLivenessMapper.selectLastLivenessByUserId(AppContextHolder.getUserId());
            if (liveness == null) {
                liveness = new SensetimeLiveness();
            }
            liveness.setCuserId(AppContextHolder.getUserId());
            liveness.setLivenessImage(path + filename);
            liveness.setLivenessFaceImage(path + filename);
            liveness.setStatus(1);
            liveness.setUpdatedDate(new Date());
            sensetimeLivenessMapper.saveOrUpdate(liveness);

            return uploadProperties.getFilepath() + filename;
        }
        return "";
    }

}
