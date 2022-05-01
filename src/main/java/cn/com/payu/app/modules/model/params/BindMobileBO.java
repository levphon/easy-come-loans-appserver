package cn.com.payu.app.modules.model.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BindMobileBO {

    @NotBlank(message = "手机号码不能为空")
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    @NotNull(message = "绑定授权账号关联的ID不能为空")
    private Long authRelId;

    @NotNull(message = "APP客户端ID不能为空")
    private String clientId;

}
