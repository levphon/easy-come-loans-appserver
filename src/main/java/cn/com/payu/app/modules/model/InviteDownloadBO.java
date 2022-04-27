package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InviteDownloadBO {

    private String inviteCode;

    @NotNull(message = "手机号码不能为空")
    private String mobile;

    @NotNull(message = "下载类型不能为空")
    private String type;

}
