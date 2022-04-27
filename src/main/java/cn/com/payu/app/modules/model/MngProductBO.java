package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class MngProductBO {

    private Long id;
    private String logo;
    @NotBlank(message = "产品名称不能为空")
    private String name;
    private String tags;
    private Integer periods;
    private BigDecimal maxAmount;
    private Float dayInterestRate;
    private BigDecimal unitPrice;
    private Integer usedCnt;
    private String url;
    @NotNull(message = "产品会员级别不能为空")
    private Integer type;
    private String remark;
    private Integer orderNum;
    private Integer position;

}
