package cn.com.payu.app.modules.model.params;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserLoanBO {

    private Integer loanPeriod;

    private BigDecimal loanAmount;

}
