package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class UnifiedPayResult {

    private String appId;
    private String timeStamp;
    private String nonceStr;
    private String pkgValue;
    private String signType;
    private String paySign;

    public boolean isSuccess() {
        return true;
    }

}
