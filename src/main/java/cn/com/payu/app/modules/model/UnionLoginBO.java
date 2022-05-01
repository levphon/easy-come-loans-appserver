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

}
