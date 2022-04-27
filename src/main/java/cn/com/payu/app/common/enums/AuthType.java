package cn.com.payu.app.common.enums;

import lombok.Getter;

@Getter
public enum AuthType {

    LOCAL("local"), THIRD("third");

    private String type;

    AuthType(String type) {
        this.type = type;
    }

}
