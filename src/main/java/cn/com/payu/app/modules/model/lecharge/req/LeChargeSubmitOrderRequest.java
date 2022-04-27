package cn.com.payu.app.modules.model.lecharge.req;

import lombok.Data;

@Data
public class LeChargeSubmitOrderRequest extends AbstractLeChargeRequest {

    private String sellerid;
    private String account;
    private String code;
    private String num;
    private String value;
    private String notifyurl;
    private String remark;

}
