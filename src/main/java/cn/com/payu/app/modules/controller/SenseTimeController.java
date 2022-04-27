package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.OcrIdcardStatelessBO;
import cn.com.payu.app.modules.model.sensetime.req.OcrIdcardStatelessReq;
import cn.com.payu.app.modules.model.sensetime.resp.LivenessAuthTokenResp;
import cn.com.payu.app.modules.model.sensetime.resp.LivenessDetectionResultResp;
import cn.com.payu.app.modules.model.sensetime.resp.OcrIdcardStatelessResp;
import cn.com.payu.app.modules.service.QiakrBizService;
import cn.com.payu.app.modules.service.SenseTimeService;
import cn.com.payu.app.modules.utils.AppContextHolder;
import cn.com.payu.app.modules.utils.Base64Utils;
import cn.com.payu.app.modules.utils.ThumbnailatorUtil;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.context.properties.UploadProperties;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;

@Slf4j
@RestController
@RequestMapping(value = "/sensetime")
@Api(value = "商汤认证模块", tags = {"商汤认证模块"})
public class SenseTimeController {

    @Autowired
    private UploadProperties uploadProperties;

    @Autowired
    private SenseTimeService senseTimeService;

    @Autowired
    private QiakrBizService qiakrBizService;

    /**
     * ocr身份证识别
     *
     * @param statelessBO
     * @return
     */
    @SysLog
    @PostMapping(value = "/ocridcard")
    public R ocrIdcard(@RequestBody @Validated OcrIdcardStatelessBO statelessBO) {

        String filepath = uploadProperties.getBasePath() + statelessBO.getUrl().substring(statelessBO.getUrl().lastIndexOf("/") + 1);

        //压缩图片
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ThumbnailatorUtil.compressImage(new File(filepath), baos);

        String imgBase64 = Base64Utils.ImageToBase64ByStream(baos.toByteArray());

        OcrIdcardStatelessReq req = new OcrIdcardStatelessReq();
        req.setImageBase64(imgBase64);
        req.setSide(statelessBO.getSide());
        OcrIdcardStatelessResp resp = senseTimeService.ocrIdcardStateless(req, statelessBO.getUrl(), AppContextHolder.getUserId());
        return R.ok().data(resp.getInfo());
    }

    /**
     * 活体识别获取token
     *
     * @return
     */
    @GetMapping(value = "/livenessAuthToken")
    public R livenessAuthToken() {
        LivenessAuthTokenResp tokenResp = senseTimeService.livenessAuthToken();
        return R.ok().data(tokenResp);
    }

    /**
     * 获取活体识别结果
     *
     * @return
     */
    @GetMapping(value = "/livenessDetectionResult")
    public R livenessDetectionResult(@RequestParam String bizToken) {
        LivenessDetectionResultResp resultResp = senseTimeService.livenessDetectionResult(bizToken);

        if (resultResp != null && resultResp.getPassed()) {

        }

        return R.ok().data(resultResp);
    }

    /**
     * 获取活体识别图片
     *
     * @return
     */
    @SysLog
    @GetMapping(value = "/getLivenessImage")
    public R getLivenessImage(@RequestParam String url) {
        String filepath = senseTimeService.getLivenessImage(url);
        return R.ok().data(filepath);
    }

}
