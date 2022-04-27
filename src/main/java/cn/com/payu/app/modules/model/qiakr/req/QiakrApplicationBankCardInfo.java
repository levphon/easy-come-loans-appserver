package cn.com.payu.app.modules.model.qiakr.req;

import lombok.Data;

@Data
public class QiakrApplicationBankCardInfo {

    private Integer cardType;
    private String bankCode;
    private String bankName;
    private String bankCardNum;
    private String cellPhone;

}
