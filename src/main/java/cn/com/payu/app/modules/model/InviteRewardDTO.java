package cn.com.payu.app.modules.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InviteRewardDTO {

    private BigDecimal totalAmount;
    private Integer invitedCnt;
    private Integer rewardCnt;

}
