package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PrepayBO {

    /**
     * 支付渠道
     */
    @NotNull(message = "支付渠道不能为空")
    private Integer payChannel;

    /**
     * 商品类型，参见：cn.com.payu.app.common.enums.GoodsType
     */
    @NotNull(message = "商品类型不能为空")
    private Integer goodsType;

    /**
     * 支付商品id
     */
    @NotNull(message = "支付商品不能为空")
    private Long goodsId;

    @NotNull(message = "支付金额不能为空")
    private BigDecimal amount;

    /**
     * 优惠券
     */
    private Long couponId;

    private Long orderId;

}
