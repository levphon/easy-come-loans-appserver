package cn.com.payu.app.modules.model.qiakr.resp;

import lombok.Data;

@Data
public class QiakrStockCuserCheckResp {

    private Integer isStock;
    private Integer isCanLoan;
    private Integer rejectReason;

}
