package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "t_order")
public class Order extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "cuser_id")
    private Long cuserId;

    /**
     * 订单流水号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 支付渠道:0自平台，1微信，2支付宝
     */
    @Column(name = "pay_channel")
    private Integer payChannel;

    @Column(name = "app_id")
    private String appId;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 实付金额
     */
    @Column(name = "pay_amount")
    private BigDecimal payAmount;

    /**
     * 订单类型，同商品类型
     */
    @Column(name = "order_type")
    private Integer orderType;

    /**
     * 类型：1收入，2支出
     * income and expenses
     */
    @Column(name = "ie_type")
    private Integer ieType;

    /**
     * 订单状态：0未支付，1已支付，2支付中，3支付失败，4取消支付
     */
    private Integer status;

    private String remark;

}