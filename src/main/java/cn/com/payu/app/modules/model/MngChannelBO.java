package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class MngChannelBO {

    private Long id;
    @NotBlank(message = "渠道码不能为空")
    private String channel;
    private String channelName;

    private Float discountRate;
    private BigDecimal unitPrice;

    private String remark;

}
