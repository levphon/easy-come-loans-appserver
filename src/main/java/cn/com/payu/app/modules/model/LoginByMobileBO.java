package cn.com.payu.app.modules.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class LoginByMobileBO {

    /**
     * 登录类型：1密码，2短信验证码,3联登入口
     */
    @NotNull
    private Integer loginType;

    @NotBlank(message = "手机号码不能为空")
    private String mobile;

    //@NotBlank(message = "验证码/密码不能为空")
    private String code;

}
