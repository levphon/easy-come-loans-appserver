package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UnionLoginBO {

    @NotBlank
    private String phone;

    @NotBlank
    private String channelCode;

    @NotBlank
    private String sign;

    /**
     * 来自app：1：默认橙色 2：绿色 3：黑色 4：蓝色
     */
    private Integer fromApp;

}
