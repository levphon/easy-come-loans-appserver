package cn.com.payu.app.modules.model.task;

import cn.com.payu.app.modules.entity.Order;
import cn.com.payu.app.modules.mapper.OrderMapper;
import cn.com.payu.app.common.enums.OrderStatus;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CloseOrderTask implements TimerTask {

    private Long orderId;
    private OrderMapper orderMapper;

    @Override
    public void run(Timeout timeout) {
        final Order order = this.orderMapper.selectById(orderId);
        if (order != null && order.getStatus().equals(OrderStatus.PAYING.getCode())) {
            log.info("取消超时支付订单{}", order.getOrderNo());
            this.orderMapper.closeOrder(orderId);
        }
    }

}
