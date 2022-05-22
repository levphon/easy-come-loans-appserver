package cn.com.payu.app.modules.entity;

import cn.com.payu.app.modules.model.SysUser;
import cn.com.payu.app.modules.utils.MngContextHolder;
import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Accessors(chain = true)
@Table(name = "t_mng_user")
public class MngUser extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 账户名
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * 头像
     */
    private String portrait;

    private String email;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 岗位
     */
    private Integer position;

    /**
     * 租户id
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 部门id
     */
    @Column(name = "department_id")
    private Long departmentId;

    /**
     * 上级id
     */
    @Column(name = "superior_id")
    private Long superiorId;

    /**
     * 数据权限类型：1UV,2CPA
     */
    @Column(name = "data_type")
    private Integer dataType;

    /**
     * 加密盐
     */
    private String salt;

    /**
     * 是否超级管理员：true是，false否
     */
    @Column(name = "is_admin")
    private Integer isAdmin;

    /**
     * 状态（1=启用 2=禁用）
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

    public MngUser() {
        super();
    }

    public MngUser(boolean isAdd) {
        if (isAdd) {
            SysUser user = MngContextHolder.getUser();
            this.setCreatedBy(user.getUserId());
            this.setCreatedDate(new Date());
        }
    }

}