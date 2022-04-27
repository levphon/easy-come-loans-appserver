package cn.com.payu.app.modules.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderSummaryDTO {

    private List<OrderSummaryModel> totalSummary;
    private List<OrderSummaryModel> todaySummary;

}
