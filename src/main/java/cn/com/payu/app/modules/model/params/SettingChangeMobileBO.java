package cn.com.payu.app.modules.model.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SettingChangeMobileBO {

    @NotBlank
    private String mobile;
    private String code;

}
