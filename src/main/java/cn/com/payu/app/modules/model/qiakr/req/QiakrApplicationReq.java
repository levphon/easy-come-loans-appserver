package cn.com.payu.app.modules.model.qiakr.req;

import lombok.Data;

@Data
public class QiakrApplicationReq extends AbstractQiakrRequest {

    private Long uid;
    private String appId;
    private Long orderId;
    private String registerPhone;
    private Long registerTime;
    private Integer loanAmount;
    private Integer period;
    private Integer periodUnit;
    private Long applyDate;

    private QiakrApplicationBasicInfo basicInfo;
    private QiakrApplicationIdInfo idInfo;
    private QiakrApplicationContactInfo contact;
    private QiakrApplicationBankCardInfo bankCardInfo;
    private QiakrApplicationExtendInfo extendInfo;
    private QiakrApplicationIdCardOcr idCardOcr;

}
