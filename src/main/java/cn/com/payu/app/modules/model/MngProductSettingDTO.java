package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MngProductSettingDTO {

    @NotNull
    private Long productId;
    private String shelfTime;
    private String offShelfTime;
    private Integer maxDailyUV;
    private Integer maxTotalUV;

}
