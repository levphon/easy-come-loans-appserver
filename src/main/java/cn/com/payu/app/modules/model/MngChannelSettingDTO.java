package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class MngChannelSettingDTO {

    @NotNull
    private Long channelId;
    private Float discountRate;
    private BigDecimal unitPrice;

}
