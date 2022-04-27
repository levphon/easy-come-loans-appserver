package cn.com.payu.app.common.enums;

import lombok.Getter;

@Getter
public enum LoginType {

    NATIVE("native"), WECHAT("wechat"), QQ("qq");

    private String type;

    LoginType(String type) {
        this.type = type;
    }

}
