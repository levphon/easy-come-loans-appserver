package cn.com.payu.app.modules.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsDTO {

    private Long id;

    private String name;

    private Integer goodsType;

    private String url;

    private BigDecimal originPrice;

    private BigDecimal price;

}
