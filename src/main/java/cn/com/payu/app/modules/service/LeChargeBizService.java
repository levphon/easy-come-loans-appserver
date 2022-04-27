package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.entity.Order;
import cn.com.payu.app.modules.entity.OrderChargeItem;
import cn.com.payu.app.modules.entity.OrderGoods;
import cn.com.payu.app.modules.mapper.OrderChargeItemMapper;
import cn.com.payu.app.modules.mapper.OrderGoodsMapper;
import cn.com.payu.app.modules.model.LeChargeSubmitOrderModel;
import cn.com.payu.app.modules.model.lecharge.resp.LeChargeSubmitOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class LeChargeBizService {

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Resource
    private OrderChargeItemMapper orderChargeItemMapper;

    @Autowired
    private LeChargeService leChargeService;

    public void charge(Order order) {
        OrderChargeItem chargeItem = orderChargeItemMapper.selectByOrderId(order.getId());

        OrderGoods orderGoods = orderGoodsMapper.selectByOrderId(order.getId());

        LeChargeSubmitOrderModel request = new LeChargeSubmitOrderModel();
        request.setSellerId(order.getOrderNo());
        request.setAccount(chargeItem.getAccount());
        request.setCode("auto_rec");
        request.setValue(String.valueOf(orderGoods.getGoodsPrice().intValue()));
        request.setNum(String.valueOf(1));
        request.setRemark(orderGoods.getGoodsName());
        LeChargeSubmitOrderResponse response = leChargeService.submitOrder(request);
        if (response != null && "s100".equals(response.getRecode())) {
            log.info("订单{}充值请求提交成功", order.getOrderNo());
        } else {
            log.info("订单{}充值请求提交失败", order.getOrderNo());
        }
    }

    public void finishedCharge(Order order, String state) {
        log.info("订单{}充值{}。。。(2订单成功,4订单失败,-11支付失败,其他 充值中)", order.getOrderNo(), state);
        orderChargeItemMapper.updateStatusByOrderId(order.getId(), state);
    }

}
