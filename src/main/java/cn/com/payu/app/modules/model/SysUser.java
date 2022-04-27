package cn.com.payu.app.modules.model;

import lombok.Data;

import java.util.Collection;

@Data
public class SysUser {

    private Long userId;
    private String username;
    private String account;
    private String phoneNumber;
    private Long departmentId;
    private Boolean isAdmin;
    private Integer dataType;

    /**
     * 用户角色
     */
    private Collection<Long> roleIds;

}
