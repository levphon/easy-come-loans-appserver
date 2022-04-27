package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "sys_id_record")
public class SysIdRecord extends BaseEntity {

    /**
     * 子系统ID
     */
    @Column(name = "sys_id")
    private String sysId;

    /**
     * 子系统名称
     */
    @Column(name = "sys_name")
    private String sysName;

    /**
     * 业务名称
     */
    @Column(name = "biz_name")
    private String bizName;

    /**
     * ID起始值
     */
    @Column(name = "initial_id")
    private Integer initialId;

    /**
     * ID步长类型
     */
    @Column(name = "interval_type")
    private Integer intervalType;

    /**
     * ID步长
     */
    @Column(name = "interval_value")
    private Integer intervalValue;

    /**
     * 子系统ID长度
     */
    private Integer length;

    /**
     * 状态（1=启用 2=禁用）
     */
    @Column(name = "enable_status")
    private Byte enableStatus;

}