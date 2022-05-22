package cn.com.payu.app.modules.model.params;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MngProductTipsSettingBO {

    @NotNull
    private Long productId;

    private String content;

    private Integer enableStatus;

}
