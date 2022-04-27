package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.enums.GoodsType;
import cn.com.payu.app.common.enums.OrderIEType;
import cn.com.payu.app.common.enums.OrderStatus;
import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.entity.*;
import cn.com.payu.app.modules.mapper.*;
import cn.com.payu.app.modules.model.PrepayBO;
import cn.com.payu.app.modules.model.UnifiedPayModel;
import cn.com.payu.app.modules.model.task.CloseOrderTask;
import cn.com.payu.app.modules.utils.AppContextHolder;
import com.glsx.plat.common.utils.SnowFlake;
import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PaymentService {

    @Resource
    private OrderMapper orderMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Resource
    private OrderCouponMapper orderCouponMapper;

    @Resource
    private CouponRecipientsMapper couponRecipientsMapper;

    @Autowired
    private UnifiedPayService unifiedPayService;

    @Autowired
    private HashedWheelTimer hashedWheelTimer;

    /**
     * 预支付下单，真正支付完成是回调通知
     *
     * @param prepayBO
     */
    @Transactional(rollbackFor = Exception.class)
    public Object prepay(PrepayBO prepayBO) {
        log.info("支付参数：{}", prepayBO.toString());

        // 检查商品是否存在
        Goods goods = goodsMapper.selectOnShelfById(prepayBO.getGoodsId());
        if (goods == null) {
            throw new AppServerException("当前购买的商品已下架！");
        }

        //券抵用金额
        BigDecimal usedAmount = BigDecimal.ZERO;
        if (prepayBO.getCouponId() != null) {
            CouponRecipients couponRecipients = couponRecipientsMapper.selectById(prepayBO.getCouponId());
            if (couponRecipients == null || couponRecipients.getUsedFlag() != 0) {
                // TODO: 2021/9/23 校验券的使用类型和有效期
                throw new AppServerException("优惠券已失效");
            }
            usedAmount = couponRecipients.getUsedAmount();
        }

        // 检查支付金额是否正确
        if (GoodsType.VIP.getType().equals(prepayBO.getGoodsType())) {
        } else if (GoodsType.GOODS.getType().equals(prepayBO.getGoodsType())) {
        } else if (GoodsType.YOUHUI_BUY.getType().equals(prepayBO.getGoodsType())) {
        } else if (GoodsType.CREDIT_REPORT.getType().equals(prepayBO.getGoodsType())) {
        } else if (GoodsType.PHONE_COST.getType().equals(prepayBO.getGoodsType())) {
        }

        //实际支付金额=商品价格-券抵用金额
        BigDecimal payAmount = goods.getPrice().subtract(usedAmount);
        log.info("商品价格={}，券抵用金额={}，实际应支付金额={}", goods.getPrice().toPlainString(), usedAmount.toPlainString(), payAmount.toPlainString());
        if (prepayBO.getAmount().compareTo(payAmount) != 0) {
            throw new AppServerException("支付金额有误！");
        }

        String orderNo = SnowFlake.nextSerialNumber();

        Date now = new Date();

        UnifiedPayModel model = UnifiedPayModel.builder()
                .type(prepayBO.getPayChannel())
                .tradeNo(orderNo)
                .subject(GoodsType.getTypeName(prepayBO.getGoodsType()))
                .totalAmount(prepayBO.getAmount())
                .build();

        Object result = unifiedPayService.unifiedPrePay(model);
        if (result != null) {
            //预订单
            Order order = new Order();
            order.setCuserId(AppContextHolder.getUserId());
            order.setOrderNo(orderNo);
            order.setPayChannel(prepayBO.getPayChannel());
            order.setOrderType(prepayBO.getGoodsType());
            order.setIeType(OrderIEType.EXPENSE.getType());
            order.setAmount(prepayBO.getAmount());
            order.setStatus(OrderStatus.PAYING.getCode());
            order.setCreatedDate(now);
            orderMapper.insertUseGeneratedKeys(order);

            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setOrderId(order.getId());
            orderGoods.setGoodsId(prepayBO.getGoodsId());
            orderGoods.setGoodsCode(goods.getGoodsCode());
            orderGoods.setGoodsName(goods.getName());
            orderGoods.setGoodsPrice(goods.getPrice());
            orderGoods.setCostPrice(goods.getCostPrice());
            orderGoods.setQuantity(1);
            orderGoods.setCreatedDate(now);
            orderGoodsMapper.insert(orderGoods);

            prepayBO.setOrderId(order.getId());

            if (prepayBO.getCouponId() != null) {
                OrderCoupon orderCoupon = new OrderCoupon();
                orderCoupon.setOrderId(order.getId());
                orderCoupon.setCouponId(prepayBO.getCouponId());
                orderCoupon.setQuantity(1);
                orderCouponMapper.insert(orderCoupon);
            }

            hashedWheelTimer.newTimeout(new CloseOrderTask(order.getId(), orderMapper), 30, TimeUnit.MINUTES);

            return result;
        }
        return null;
    }

}
