package cn.com.payu.app.modules.model.lecharge.req;

import lombok.Data;

@Data
public class LeChargeQueryOrderRequest extends AbstractLeChargeRequest {

    private String sellerid;

    public LeChargeQueryOrderRequest() {
    }

    public LeChargeQueryOrderRequest(String sellerid) {
        this.sellerid = sellerid;
    }

}
