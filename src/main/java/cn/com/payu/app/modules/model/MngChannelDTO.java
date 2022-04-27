package cn.com.payu.app.modules.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MngChannelDTO {

    private Long id;
    private String channel;
    private String channelName;
    private String remark;
    private Integer enableStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;
    private Integer registerCnt;
    private Integer downloadCnt;
    private Integer uv;
    private Integer cpa;

    @JSONField(format = "#0.00")
    private Float discountRate;

    @JSONField(format = "#0.00")
    private BigDecimal unitPrice;

    private Integer buyVipCnt;

    @JSONField(format = "#0.00")
    private BigDecimal buyVipAmount;

    private Integer settleRegisterCnt;
    private Integer settleRegisterRatio;

    @JSONField(format = "#0.00")
    private BigDecimal cost;

    @JSONField(format = "#0.00")
    private BigDecimal actualPrice;

    @JSONField(format = "#0.00")
    private BigDecimal uvCost;

    @JSONField(format = "#0.00")
    private Float loginOutputRate;

}
