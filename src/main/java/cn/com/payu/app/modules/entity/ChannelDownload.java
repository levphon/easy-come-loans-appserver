package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_channel_download")
public class ChannelDownload extends BaseEntity {

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

    @Column(name = "decrypt_account")
    private String decryptAccount;

    /**
     * 1有效
     */
    private Integer status;

}