package cn.com.payu.app.common.enums;

import lombok.Getter;

@Getter
public enum OrderChannel {

    NATIVE_PAY(0), WECHAT_PAY(1), ALI_PAY(2), INNER_PAY(3), QIAKR(4);

    private Integer type;

    OrderChannel(Integer type) {
        this.type = type;
    }

}
