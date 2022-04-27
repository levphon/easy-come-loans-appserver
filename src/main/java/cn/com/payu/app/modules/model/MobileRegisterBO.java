package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MobileRegisterBO {

    @NotBlank(message = "手机号码不能为空")
    private String mobile;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String code;

    private String clientId;

    private String os;
    private String version;
    private String imei;
    private String channel;

    /**
     * 来自app：1：默认橙色 2：绿色 3：黑色 4：蓝色
     */
    private Integer fromApp;

}
