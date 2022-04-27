package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "t_coupon")
public class Coupon extends BaseEntity {

    /**
     * 优惠劵名称(有图片则显示图片)
     */
    private String title;
    /**
     * 图片
     */
    private String icon;
    /**
     * 可用于：10店铺优惠券 11新人店铺券  20商品优惠券  30类目优惠券 60平台优惠券 61新人平台券
     */
    private Integer used;
    /**
     * 1抵用券 2满减券 3无门槛券（需要限制大小）
     */
    private Integer type;
    /**
     * 1可用于特价商品 2不能 默认不能(商品优惠卷除外)
     */
    @Column(name = "with_special")
    private Integer withSpecial;
    /**
     * 店铺或商品流水号/id,如果used=20券作用于商品，used=30券作用于整个商品类目
     */
    @Column(name = "with_sn")
    private String withSn;
    /**
     * 满多少金额
     */
    @Column(name = "with_amount")
    private BigDecimal withAmount;
    /**
     * 劵成本价
     */
    @Column(name = "cost_amount")
    private BigDecimal costAmount;
    /**
     * 劵抵用金额
     */
    @Column(name = "used_amount")
    private BigDecimal usedAmount;
    /**
     * 单个（商品）使用配额：针对单个商品、服务等使用券的个数最大限制
     */
    private Integer quota;
    /**
     * 总配额：发（行）券数量
     */
    @Column(name = "total_quota")
    private Integer totalQuota;
    /**
     * 已领取的优惠券数量
     */
    @Column(name = "take_count")
    private Integer takeCount;
    /**
     * 已使用的优惠券数量
     */
    @Column(name = "used_count")
    private Integer usedCount;
    /**
     * 发放开始时间
     */
    @Column(name = "start_time")
    private Date startTime;
    /**
     * 发放结束时间
     */
    @Column(name = "end_time")
    private Date endTime;
    /**
     * 时效:1绝对时效（领取后XXX-XXX时间段有效），2相对时效（领取后N天有效）
     */
    @Column(name = "valid_type")
    private Integer validType;
    /**
     * 使用开始时间
     */
    @Column(name = "valid_start_time")
    private Date validStartTime;
    /**
     * 使用结束时间
     */
    @Column(name = "valid_end_time")
    private Date validEndTime;
    /**
     * 自领取之日起有效天数
     */
    @Column(name = "valid_days")
    private Integer validDays;
    /**
     * 单个商品用券数量
     */
    @Column(name = "used_quantity")
    private Integer usedQuantity;
    /**
     * 备注
     */
    private String remark;
    /**
     * 1生效 2失效 3已结束
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

}