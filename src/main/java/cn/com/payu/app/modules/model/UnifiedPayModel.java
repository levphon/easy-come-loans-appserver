package cn.com.payu.app.modules.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UnifiedPayModel {

    private Integer type;
    private String tradeNo;
    private String subject;
    /**
     * 注意：微信单位是分，支付宝单位是元
     */
    private BigDecimal totalAmount;

}
