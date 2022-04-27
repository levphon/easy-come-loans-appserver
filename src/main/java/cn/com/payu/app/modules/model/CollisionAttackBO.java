package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CollisionAttackBO {

    @NotBlank
    private String phoneMd5;

    @NotBlank
    private String channelCode;

    @NotBlank
    private String sign;

}
