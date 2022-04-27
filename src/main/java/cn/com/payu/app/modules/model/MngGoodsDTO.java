package cn.com.payu.app.modules.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MngGoodsDTO {

    private Long id;
    private String name;
    private String goodsCode;
    private Integer goodsType;
    private BigDecimal originPrice;
    private BigDecimal price;
    private BigDecimal costPrice;
    private String remark;
    private Integer orderNum;
    private Integer enableStatus;

}
