package cn.com.payu.app.modules.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderSummaryModel {

    private String goodsCode;
    private String goodsName;
    private Integer orderCnt;
    private BigDecimal orderAmount;

}
