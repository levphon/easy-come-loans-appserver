package cn.com.payu.app.modules.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticsModel {

    private String label;
    private Integer cnt;
    private BigDecimal amount;

    public StatisticsModel() {

    }

    public StatisticsModel(String label, Integer cnt) {
        this.label = label;
        this.cnt = cnt;
    }

    public StatisticsModel(String label, Integer cnt, BigDecimal amount) {
        this.label = label;
        this.cnt = cnt;
        this.amount = amount;
    }

}
