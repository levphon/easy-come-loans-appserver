package cn.com.payu.app.modules.model.qiakr.req;

import lombok.Data;

@Data
public class QiakrApplicationIdInfo {

    /**
     * 身份证号
     */
    private String cid;
    /**
     * 姓名
     */
    private String name;
    private Long checkTime;
    private Integer gender;
    private String address;
    private String issuedBy;
    private String validDate;
    private String nation;
    private String birthday;

}
