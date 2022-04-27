package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_order_coupon")
public class OrderCoupon extends BaseEntity {

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 券id
     */
    @Column(name = "coupon_id")
    private Long couponId;

    /**
     * 券数量
     */
    private Integer quantity;

}