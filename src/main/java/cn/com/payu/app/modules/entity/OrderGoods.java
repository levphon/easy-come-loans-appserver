package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "t_order_goods")
public class OrderGoods extends BaseEntity {

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 商品id
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品代码
     */
    @Column(name = "goods_code")
    private String goodsCode;

    /**
     * 商品价格，单位元
     */
    @Column(name = "goods_price")
    private BigDecimal goodsPrice;

    /**
     * 成本价，单位元
     */
    @Column(name = "cost_price")
    private BigDecimal costPrice;

    /**
     * 购买的商品链接
     */
    @Column(name = "goods_url")
    private String goodsUrl;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 订单商品状态（看具体商品，正常与order.status一致，如果外部服务商商品，则同他们一致）
     */
    private String status;

}