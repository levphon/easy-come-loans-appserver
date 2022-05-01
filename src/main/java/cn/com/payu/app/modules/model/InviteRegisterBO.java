package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InviteRegisterBO {

    private String inviteCode;

    @NotNull(message = "手机号码不能为空")
    private String mobile;

    //@NotNull(message = "验证码不能为空")
    private String code;

}
