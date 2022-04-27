package cn.com.payu.app.modules.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MngOrderDetailsDTO {

    private Long id;

    private String orderNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;

    private Integer orderType;

    private String realName;

    private BigDecimal amount;

    private String account;

    private String channel;

    private OrderAddressDTO oaInfo;

}