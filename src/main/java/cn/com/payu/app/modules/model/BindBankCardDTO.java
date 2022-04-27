package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class BindBankCardDTO {

    private Long id;
    private String bankCode;
    private String bankName;
    private String accountName;
    private String accountNo;

}
