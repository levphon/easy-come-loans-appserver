package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class MngProductInfoDTO {

    private Long id;
    private String logo;
    private String name;
    private String tags;
    private String city;
    private String cityCode;
    private String periods;
    private BigDecimal maxAmount;
    private Float dayInterestRate;
    private Integer usedCnt;
    private BigDecimal unitPrice;
    private String url;
    private Integer type;
    private Integer orderNum;
    private Integer position;
    private String remark;

}
