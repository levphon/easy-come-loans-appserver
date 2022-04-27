package cn.com.payu.app.modules.model.lecharge.resp;

import lombok.Data;

@Data
public class LeChargeQueryOrderResponse extends AbstractLeChargeResponse {

    private String sellerid;
    private String code;
    private String value;
    private String payvalue;
    private String voucher;
    private String createtime;
    private String endtime;
    private String state;

}
