package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class ProductDTO {

    private Long id;
    private String channel;
    private String name;
    private String logo;
    private String tags;
    private String city;
    private String cityCode;
    private BigDecimal maxAmount;
    private Float dayInterestRate;
    private Integer usedCnt;
    private Integer type;
    private String remark;

}
