package cn.com.payu.app.modules.model.params;

import lombok.Data;

/**
 * @author payu
 */
@Data
public class BindBankCardBO {

    private String bankCode;
    private String bankName;
    private String accountName;
    private String accountNo;

}
