package cn.com.payu.app.modules.entity;

import cn.com.payu.app.modules.model.SysUser;
import cn.com.payu.app.modules.utils.MngContextHolder;
import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Accessors(chain = true)
@Data
@Table(name = "t_department")
public class Department extends BaseEntity {

    public Department() {
        super();
    }

    public Department(boolean isAdd) {
        if (isAdd) {
            SysUser user = MngContextHolder.getUser();
            this.setCreatedBy(user.getUserId());
            this.setCreatedDate(new Date());
        }
    }

    /**
     * 部门名称
     */
    @Column(name = "department_name")
    private String departmentName;

    /**
     * 租户id
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 排序值
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 组织（部门）类型(预留字段，依据租户自己具体情况分类，建议建立数据字典)
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 是否根部门（0=否 1=是）
     */
    @Column(name = "is_root")
    private Integer isRoot;

    /**
     * 状态（1=启用 2=禁用）
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

    @Transient
    private Long userId;

}