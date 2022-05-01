package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.enums.GoodsType;
import cn.com.payu.app.modules.model.PrepayBO;
import cn.com.payu.app.modules.model.UnifiedPayModel;
import com.glsx.plat.common.utils.SnowFlake;
import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class PaymentService {

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

        // 检查支付金额是否正确

        //实际支付金额=商品价格-券抵用金额
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
            return result;
        }
        return null;
    }

}
