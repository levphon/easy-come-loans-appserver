package cn.com.payu.app.modules.model.task;

import cn.com.payu.app.common.enums.OrderStatus;
import cn.com.payu.app.modules.entity.CouponRecipients;
import cn.com.payu.app.modules.mapper.CouponRecipientsMapper;
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
public class CloseCouponTask implements TimerTask {

    private Long recipientsCouponId;
    private CouponRecipientsMapper couponRecipientsMapper;

    @Override
    public void run(Timeout timeout) {
        final CouponRecipients recipients = this.couponRecipientsMapper.selectById(recipientsCouponId);
        if (recipients != null && recipients.getUsedFlag().equals(OrderStatus.UNPAID.getCode())) {
            log.info("取消领取的超时会员优惠券{}", recipients.getId());
            this.couponRecipientsMapper.closeCoupon(recipientsCouponId);
        }
    }

}
