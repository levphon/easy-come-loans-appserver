package cn.com.payu.app.modules.utils;

import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.config.LeChargeConfig;
import cn.com.payu.app.modules.model.lecharge.req.AbstractLeChargeRequest;
import cn.com.payu.app.modules.model.lecharge.req.LeChargeGetBalanceRequest;
import cn.com.payu.app.modules.model.lecharge.req.LeChargeQueryOrderRequest;
import cn.com.payu.app.modules.model.lecharge.req.LeChargeSubmitOrderRequest;
import com.alibaba.fastjson.JSONObject;
import com.glsx.plat.common.utils.DateUtils;
import com.glsx.plat.common.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class LeChargeUtils {

    @Autowired
    private LeChargeConfig leChargeConfig;

    /**
     * 调用服务
     */
    public <T> T access(String subRoute, Object req, Class<T> clazz) {
        String url = leChargeConfig.getBaseApiUrl() + subRoute;
        return accessUrl(url, req, clazz);
    }

    /**
     * 调用乐购充值
     */
    private <T> T accessUrl(String url, Object req, Class<T> clazz) throws AppServerException {

        AbstractLeChargeRequest paramReq = (AbstractLeChargeRequest) req;

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
    private void complementedRequestData(AbstractLeChargeRequest paramReq) throws AppServerException {
        paramReq.setAgentcode(leChargeConfig.getAgentCode());
        paramReq.setTime(DateUtils.formatSerial(new Date()));

        String sign = "";
        if (paramReq instanceof LeChargeGetBalanceRequest) {
            sign = new LeChargeMD5().calcMD5(paramReq.getAgentcode() + paramReq.getTime() + leChargeConfig.getSecretKey());
        } else if (paramReq instanceof LeChargeSubmitOrderRequest) {
            LeChargeSubmitOrderRequest request = (LeChargeSubmitOrderRequest) paramReq;


            Base64 base64 = new Base64();

            String account_base64 = base64.encodeToString(request.getAccount().getBytes(StandardCharsets.UTF_8));
            String remark_base64 = base64.encodeToString(request.getRemark().getBytes(StandardCharsets.UTF_8));

            sign = new LeChargeMD5().calcMD5(paramReq.getAgentcode() +
                    request.getSellerid() +
                    account_base64 +
                    request.getCode() +
                    request.getNum() +
                    request.getValue() +
                    leChargeConfig.getNotifyUrl() +
                    remark_base64 +
                    paramReq.getTime() + leChargeConfig.getSecretKey());

            request.setAccount(account_base64);
            request.setRemark(remark_base64);
            request.setNotifyurl(leChargeConfig.getNotifyUrl());
        } else if (paramReq instanceof LeChargeQueryOrderRequest) {
            LeChargeQueryOrderRequest request = (LeChargeQueryOrderRequest) paramReq;

            sign = new LeChargeMD5().calcMD5(paramReq.getAgentcode() + request.getSellerid() + paramReq.getTime() + leChargeConfig.getSecretKey());
        }
        paramReq.setSign(sign);
    }


    /**
     * 校验请求实体的通用属性,比如appkey属性及sign属性。
     * 也可以通过 instanceof,针对特定请求实体进行校验操作。
     */
    private void verifyRequestData(AbstractLeChargeRequest req) {

    }

    /**
     * 真正调用网络请求方法
     *
     * @param url
     * @param paramReq
     * @param <T>
     * @return
     */
    private <T> T realCall(String url, AbstractLeChargeRequest paramReq, Class<T> clazz) throws AppServerException {
        JSONObject params = (JSONObject) JSONObject.toJSON(paramReq);
        log.info("【乐购充值】http请求信息—— url[{}] params[{}]", url, params.toJSONString());
        String result = HttpUtils.post(url, params);
        log.info("【乐购充值】http响应信息—— result[{}]", result);
        return (T) JSONObject.parseObject(result, clazz);
    }

}
