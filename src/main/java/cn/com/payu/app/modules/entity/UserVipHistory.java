package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "t_cust_user_vip_history")
public class UserVipHistory extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 开通vip项目id
     */
    @Column(name = "vip_id")
    private Long vipId;

    /**
     * 实际花费
     */
    @Column(name = "actual_cost")
    private BigDecimal actualCost;

    /**
     * 开通来源：0用户自己开通，1内部充值开通
     */
    private Integer type;

    /**
     * 开通数量（单位月）
     */
    private Integer quantity;

    /**
     * 生效时间
     */
    @Column(name = "effective_date")
    private Date effectiveDate;

    /**
     * 失效时间
     */
    @Column(name = "expiration_date")
    private Date expirationDate;

    /**
     * 开通状态：0未开通，1开通，2失效
     */
    private Integer status;

}