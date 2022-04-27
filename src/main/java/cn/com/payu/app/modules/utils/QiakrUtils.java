package cn.com.payu.app.modules.utils;

import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.config.QiakrConfig;
import cn.com.payu.app.modules.model.qiakr.req.*;
import cn.com.payu.app.modules.model.qiakr.resp.AbstractQiakrResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.glsx.plat.common.utils.HttpUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class QiakrUtils {

    @Autowired
    private QiakrConfig qiakrConfig;

    /**
     * 调用服务
     */
    public <T> T access(String subRoute, Object req, Class<T> clazz) {
        String url = qiakrConfig.getBaseApiUrl() + subRoute;
        return accessUrl(url, req, clazz);
    }

    /**
     * 调用洽客
     */
    private <T> T accessUrl(String url, Object req, Class<T> clazz) throws AppServerException {

        AbstractQiakrRequest paramReq = (AbstractQiakrRequest) req;

        // 1. 补全请求数据，封装参数
        complementedRequestData(paramReq);

        // 2. 校验请求数据
        verifyRequestData(paramReq);

        // 3. 网络请求,获取结果
        return realCall(url, paramReq, clazz);
    }

    /**
     * 补全请求实体—— 补全通用的实体属性,比如appkey属性及sign属性。
     * 也可以通过 instanceof,针对特定请求实体进行补全操作。
     */
    private void complementedRequestData(AbstractQiakrRequest paramReq) throws AppServerException {
        paramReq.setChannelId(qiakrConfig.getChannelId());
        paramReq.setTimestamp(System.currentTimeMillis());

        //通过JsonConfig的对象，来去除，json数据中不要的字段
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("channelId");
        filter.getExcludes().add("timestamp");
        filter.getExcludes().add("sign");
        filter.getExcludes().add("request");

        String request = "";
        if (paramReq instanceof QiakrStockCuserCheckReq) {
            QiakrStockCuserCheckReq checkReq = (QiakrStockCuserCheckReq) paramReq;

            request = JSON.toJSONString(checkReq, filter);
        } else if (paramReq instanceof QiakrApplicationReq) {
            QiakrApplicationReq applicationReq = (QiakrApplicationReq) paramReq;

            request = JSON.toJSONString(applicationReq, filter);
        } else if (paramReq instanceof QiakrAuditResultReq) {
            QiakrAuditResultReq auditResultReq = (QiakrAuditResultReq) paramReq;

            request = JSON.toJSONString(auditResultReq, filter);
        } else if (paramReq instanceof QiakrGetProductUrlReq) {
            QiakrGetProductUrlReq getProductUrlReq = (QiakrGetProductUrlReq) paramReq;

            request = JSON.toJSONString(getProductUrlReq, filter);
        } else if (paramReq instanceof QiakrAgreementReq) {
            QiakrAgreementReq agreementReq = (QiakrAgreementReq) paramReq;

            request = JSON.toJSONString(agreementReq, filter);
        }
        String encryptRequest = BairongSignature.encryptAES(qiakrConfig.getAesKey(), request);
        paramReq.setRequest(encryptRequest);

        Map<String, String> signMap = Maps.newTreeMap();
        signMap.put("channelId", qiakrConfig.getChannelId());
        signMap.put("timestamp", String.valueOf(paramReq.getTimestamp()));
        signMap.put("request", encryptRequest);
        String sign = BairongSignature.signSHA1(signMap, qiakrConfig.getPrivateKey());
        log.info("签名：{}，签名参数：{}，原始业务参数：{}", sign, signMap, request);
        paramReq.setSign(sign);
    }


    /**
     * 校验请求实体的通用属性,比如appkey属性及sign属性。
     * 也可以通过 instanceof,针对特定请求实体进行校验操作。
     */
    private void verifyRequestData(AbstractQiakrRequest req) {

    }

    /**
     * 真正调用网络请求方法
     *
     * @param url
     * @param paramReq
     * @param <T>
     * @return
     */
    private <T> T realCall(String url, AbstractQiakrRequest paramReq, Class<T> clazz) throws AppServerException {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("channelId", qiakrConfig.getChannelId());
        paramMap.put("timestamp", String.valueOf(paramReq.getTimestamp()));
        paramMap.put("request", paramReq.getRequest());
        paramMap.put("sign", paramReq.getSign());
        log.info("【洽客】http请求信息—— url[{}] params[{}]", url, paramMap);
        String result = HttpUtils.post(url, JSONUtil.toJsonStr(paramMap));
        log.info("【洽客】http响应信息—— result[{}]", result);
        AbstractQiakrResponse response = JSONObject.parseObject(result, AbstractQiakrResponse.class);
        String decryptResponse = BairongSignature.decryptAES(qiakrConfig.getAesKey(), response.getResponse());
        // TODO: 2022/1/4 判断是数组还是对象
        return JSONObject.parseObject(decryptResponse, clazz);
    }

}
