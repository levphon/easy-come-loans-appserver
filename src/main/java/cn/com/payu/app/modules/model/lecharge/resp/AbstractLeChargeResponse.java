package cn.com.payu.app.modules.model.lecharge.resp;

import lombok.Data;

@Data
public abstract class AbstractLeChargeResponse {

    private String recode;
    private String msg;
    private String agentcode;
    private String retime;

}
