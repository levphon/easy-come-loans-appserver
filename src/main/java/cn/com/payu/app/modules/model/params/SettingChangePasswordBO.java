package cn.com.payu.app.modules.model.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SettingChangePasswordBO {

    @NotBlank
    private String newPassword;
    private String code;

}
