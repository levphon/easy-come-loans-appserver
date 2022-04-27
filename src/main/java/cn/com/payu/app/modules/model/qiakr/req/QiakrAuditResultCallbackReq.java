package cn.com.payu.app.modules.model.qiakr.req;

import cn.com.payu.app.modules.model.qiakr.resp.QiakrAuditResultAmountOptionResp;
import lombok.Data;

@Data
public class QiakrAuditResultCallbackReq {

    private Integer orderId;
    private Integer status;
    private String remark;
    private QiakrAuditResultAmountOptionResp amountOption;

}
