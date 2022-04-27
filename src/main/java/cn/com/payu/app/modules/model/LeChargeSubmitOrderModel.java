package cn.com.payu.app.modules.model;

import cn.com.payu.app.modules.model.lecharge.req.AbstractLeChargeRequest;
import lombok.Data;

@Data
public class LeChargeSubmitOrderModel extends AbstractLeChargeRequest {

    private String sellerId;
    private String account;
    private String code;
    private String num;
    private String value;
    private String remark;

}
