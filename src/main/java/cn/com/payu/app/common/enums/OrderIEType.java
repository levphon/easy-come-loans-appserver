package cn.com.payu.app.common.enums;

import lombok.Getter;

@Getter
public enum OrderIEType {

    INCOME("收入", 1), EXPENSE("支出", 2);

    private String name;
    private Integer type;

    OrderIEType(String name, Integer type) {
        this.name = name;
        this.type = type;
    }

    public static String getTypeName(Integer type) {
        for (OrderIEType ot : values()) {
            if (ot.type.equals(type)) {
                return ot.name;
            }
        }
        return "";
    }

}
