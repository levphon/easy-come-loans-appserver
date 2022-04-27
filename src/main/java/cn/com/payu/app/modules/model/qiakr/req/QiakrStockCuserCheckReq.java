package cn.com.payu.app.modules.model.qiakr.req;

import lombok.Data;

@Data
public class QiakrStockCuserCheckReq extends AbstractQiakrRequest {

    private String name;
    private String md5;
    private String mobileMd5;

}
