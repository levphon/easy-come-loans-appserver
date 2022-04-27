package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "t_vip")
public class Vip extends BaseEntity {

    /**
     * 期限
     */
    private Integer during;
    /**
     * 期限单位：1日，2月，3年
     */
    @Column(name = "during_type")
    private Integer duringType;
    /**
     * 价格，单位（元）
     */
    private BigDecimal price;

    /**
     * 估值
     */
    private String worth;

    /**
     * 排序
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 状态（1=启用 2=禁用）
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

}