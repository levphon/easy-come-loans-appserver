package cn.com.payu.app.modules.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsDetailsDTO {

    private Long id;

    private String url;

    private String name;

    private Integer goodsType;

    private BigDecimal originPrice;

    private BigDecimal price;

    private String remark;

}
