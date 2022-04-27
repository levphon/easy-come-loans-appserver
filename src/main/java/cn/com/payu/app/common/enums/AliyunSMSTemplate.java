package cn.com.payu.app.common.enums;

import lombok.Getter;

@Getter
public enum AliyunSMSTemplate {

    AUTHENTICATION("SMS_209905223"),
    LOGIN_CONFIRM("SMS_209905222"),
    LOGIN_EXCEPTION("SMS_209905221"),
    USER_REGISTRY("SMS_209905220"),
    CHANGE_PASSWORD("SMS_209905219"),
    INFORMATION_CHANGE("SMS_209905218");

    private String type;

    AliyunSMSTemplate(String type) {
        this.type = type;
    }

}
