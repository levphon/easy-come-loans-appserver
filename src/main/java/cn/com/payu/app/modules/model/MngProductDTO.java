package cn.com.payu.app.modules.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MngProductDTO {

    private Long id;
    private String productName;
    private String periods;
    private BigDecimal maxAmount;
    private Float dayInterestRate;
    private Integer type;
    private String os;
    private String remark;
    private Integer orderNum;
    private Integer enableStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;
    private Integer clickCnt;

}
