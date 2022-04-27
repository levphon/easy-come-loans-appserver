package cn.com.payu.app.modules.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CustUserRegisterListDTO {

    private Long userId;
    private String realName;
    private String identityNo;
    private String mobile;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerDate;
    private Integer fromApp;

    private String bankName;
    private String bankAccount;

    private BigDecimal loanAmount;
    private Integer loanPeriod;

    private Integer vipStatus;
    private String vipStatusStr;

    private Integer vipDays;
    private Integer vipExpireDays;

    private Integer delFlag;

}
