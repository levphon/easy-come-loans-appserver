package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class AlipayConfigModel {

    private String appId;
    private String privateKey;
    private String publicKey;
    private String notifyUrl;
    private String returnUrl;
    private String signType;
    private String charset;
    private String gatewayUrl;

}
