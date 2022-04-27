package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.persistence.Column;

@Data
public class ChannelDownloadUserDTO {

    private Long id;

    /**
     * 渠道标识
     */
    private String channel;

    /**
     * 下载平台
     */
    private String platform;

    /**
     * 用户账号（手机号）
     */
    private String account;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 身份证号
     */
    @Column(name = "identity_no")
    private String identityNo;

}
