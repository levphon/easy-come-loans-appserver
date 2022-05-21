package cn.com.payu.app.modules.model;

import cn.com.payu.app.modules.entity.Department;
import cn.com.payu.app.modules.entity.Role;
import cn.com.payu.app.modules.entity.Tenant;
import lombok.Data;

import java.util.List;
import java.util.Set;

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
     * 用户所属租户
     */
    private Tenant tenant;

    /**
     * 用户所属部门
     */
    private Department department;

    /**
     * 用户角色
     */
    private List<Role> roles;

    /**
     * 数据创建人id
     */
    private Set<Long> visibleCreatorIds;


}
