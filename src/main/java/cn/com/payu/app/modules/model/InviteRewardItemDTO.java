package cn.com.payu.app.modules.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InviteRewardItemDTO {

    private Long orderId;
    private Long cuserId;
    private String username;
    private String avatar;
    private String orderNo;
    private BigDecimal amount;
    private String sourceExplain;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;

}
