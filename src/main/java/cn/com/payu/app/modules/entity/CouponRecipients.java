package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "t_coupon_recipients")
public class CouponRecipients extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 优惠券id，系统配置的优惠券项目
     */
    @Column(name = "coupon_id")
    private Long couponId;

    /**
     * 优惠券名称
     */
    @Column(name = "coupon_title")
    private String couponTitle;

    /**
     * 可用于：80会员券
     */
    private Integer used;

    /**
     * 1抵用券 2满减券 3无门槛券（需要限制大小）
     */
    private Integer type;

    /**
     * 订单/商品流水号/id
     */
    @Column(name = "with_sn")
    private String withSn;

    /**
     * 领券时劵抵用金额
     */
    @Column(name = "used_amount")
    private BigDecimal usedAmount;

    /**
     * 领券时抵用券数量
     */
    @Column(name = "used_quantity")
    private Integer usedQuantity;

    /**
     * 使用门槛：1无使用门槛，2XXX
     */
    @Column(name = "used_threshold")
    private Integer usedThreshold;

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
     * 券使用状态：0未使用，1已使用，2已失效
     */
    @Column(name = "used_flag")
    private Integer usedFlag;

}