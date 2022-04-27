package cn.com.payu.app.modules.model.lecharge.req;

import lombok.Data;

@Data
public abstract class AbstractLeChargeRequest {

    private String agentcode;
    private String time;
    private String sign;

}
