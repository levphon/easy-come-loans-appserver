package cn.com.payu.app.modules.model.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrgModel {

    private Long orgId;
    private String orgName;
    private Long parentId;
    private Long tenantId;
    /**
     * 是否根组织：0否，1是
     */
    private Integer isRoot;

    /**
     * 组织类型：o组织，u用户
     */
    private String type = "";

    @JsonIgnore
    private Integer depth;
    @JsonIgnore
    private Integer userNumber;
    @JsonIgnore
    private Integer orderNum;

    public Integer getUserNumber() {
        return userNumber == null ? 0 : userNumber;
    }

}
