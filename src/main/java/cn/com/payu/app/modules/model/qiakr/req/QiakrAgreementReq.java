package cn.com.payu.app.modules.model.qiakr.req;

import lombok.Data;

@Data
public class QiakrAgreementReq extends AbstractQiakrRequest {

    private Integer uid;
    private Integer orderId;
    private Integer optType;

}
