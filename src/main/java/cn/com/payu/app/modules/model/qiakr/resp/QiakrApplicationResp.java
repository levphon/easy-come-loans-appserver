package cn.com.payu.app.modules.model.qiakr.resp;

import lombok.Data;

@Data
public class QiakrApplicationResp {


    /**
     * 业务状态码
     * 2001-进件成功
     * 2003-进件失败
     */
    private Integer status;

    /**
     * 失败原因(失败时必填)
     */
    private String remark;

}
