package cn.com.payu.app.common.enums;

import lombok.Getter;

@Getter
public enum GoodsType {

    VIP("订阅会员", 80),
    GOODS("购买商品", 90),
    PHONE_COST("充值话费", 100),
    YOUHUI_BUY("优惠购", 110),
    CREDIT_REPORT("信用报告", 120),
    ;

    private String name;
    private Integer type;

    GoodsType(String name, Integer type) {
        this.name = name;
        this.type = type;
    }

    public static String getTypeName(Integer type) {
        for (GoodsType ot : values()) {
            if (ot.type.equals(type)) {
                return ot.name;
            }
        }
        return "";
    }

}
