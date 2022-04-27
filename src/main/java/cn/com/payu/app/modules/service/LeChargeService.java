package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.model.LeChargeSubmitOrderModel;
import cn.com.payu.app.modules.model.lecharge.req.LeChargeGetBalanceRequest;
import cn.com.payu.app.modules.model.lecharge.req.LeChargeQueryOrderRequest;
import cn.com.payu.app.modules.model.lecharge.req.LeChargeSubmitOrderRequest;
import cn.com.payu.app.modules.model.lecharge.resp.LeChargeGetBalanceResponse;
import cn.com.payu.app.modules.model.lecharge.resp.LeChargeQueryOrderResponse;
import cn.com.payu.app.modules.model.lecharge.resp.LeChargeSubmitOrderResponse;
import cn.com.payu.app.modules.utils.LeChargeUtils;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LeChargeService {

    @Autowired
    private LeChargeUtils leChargeUtils;

    public LeChargeGetBalanceResponse getBalance() {
        LeChargeGetBalanceResponse response = leChargeUtils.access("getbalance", new LeChargeGetBalanceRequest(), LeChargeGetBalanceResponse.class);
        log.info(JSONUtil.toJsonPrettyStr(response));
        return response;
    }


    public LeChargeSubmitOrderResponse submitOrder(LeChargeSubmitOrderModel model) {
        LeChargeSubmitOrderRequest request = new LeChargeSubmitOrderRequest();
        request.setSellerid(model.getSellerId());
        request.setAccount(model.getAccount());
        request.setCode(model.getCode());
        request.setNum(model.getNum());
        request.setValue(model.getValue());
        request.setRemark(model.getRemark());
        LeChargeSubmitOrderResponse response = leChargeUtils.access("submitorder", request, LeChargeSubmitOrderResponse.class);
        log.info(JSONUtil.toJsonPrettyStr(response));
        return response;
    }

    public LeChargeQueryOrderResponse queryOrder(String sellerId) {
        LeChargeQueryOrderResponse response = leChargeUtils.access("queryorder", new LeChargeQueryOrderRequest(sellerId), LeChargeQueryOrderResponse.class);
        log.info(JSONUtil.toJsonPrettyStr(response));
        return response;
    }

}
