package cn.com.payu.app.common.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    UNPAID(0, "未支付"),
    PAID(1, "已支付"),
    PAYING(2, "支付中"),
    FAIL(3, "支付失败"),
    CANCEL(4, "取消支付"),
    REFUND(5, "退款");

    private Integer code;
    private String name;

    OrderStatus(Integer type, String name) {
        this.code = type;
        this.name = name;
    }

}
