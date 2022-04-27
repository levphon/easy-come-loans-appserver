package cn.com.payu.app.modules.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CouponDTO {

    private Long id;
    private String title;
    private String icon;
    private Integer used;
    private BigDecimal withAmount;
    private BigDecimal usedAmount;

}
