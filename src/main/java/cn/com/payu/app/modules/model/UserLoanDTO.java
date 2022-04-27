package cn.com.payu.app.modules.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserLoanDTO {

    private Integer loanPeriod;

    private BigDecimal loanAmount;

}
