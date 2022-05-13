package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class MngProductBO {

    private Long id;

    @NotBlank(message = "产品名称不能为空")
    private String name;
    private String logo;
    private String tags;
    private String province;
    private String provinceCode;
    private String city;
    private String cityCode;
    private String os;

    private String periods;
    private BigDecimal maxAmount;
    private Float dayInterestRate;
    private BigDecimal unitPrice;
    private Integer usedCnt;
    private String url;
    private String remark;
    private Integer orderNum;
    private Integer position;

}
