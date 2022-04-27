package cn.com.payu.app.modules.model.qiakr.req;

import lombok.Data;

@Data
public class QiakrAuditResultReq extends AbstractQiakrRequest {

    private Long orderId;
    private Long uid;

}