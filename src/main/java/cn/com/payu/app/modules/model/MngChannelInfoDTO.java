package cn.com.payu.app.modules.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MngChannelInfoDTO {

    private Long id;
    private String channel;
    private String channelName;
    private Float discountRate;
    private BigDecimal unitPrice;
    private String remark;

}
