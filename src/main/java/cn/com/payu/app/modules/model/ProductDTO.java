package cn.com.payu.app.modules.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {

    private Long id;
    private String channel;
    private String name;
    private String logo;
    private String tags;
    private BigDecimal maxAmount;
    private Float dayInterestRate;
    private Integer usedCnt;
    private Integer type;
    private String remark;

}
