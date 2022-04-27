package cn.com.payu.app.modules.model.qiakr.req;

import lombok.Data;

@Data
public class QiakrApplicationBasicInfo {

    private String houseProvince;
    private String houseCity;
    private String houseDistrict;//否
    private String houseAddress;
    private String companyName;
    private String companyProvince;
    private String companyCity;
    private String companyDistrict;//否
    private String unitAddress;
    private String degree;
    private String marriage;

}
