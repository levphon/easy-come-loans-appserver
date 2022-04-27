package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_cust_user_app")
public class UserApp extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 消息推送相关客户端id
     */
    @Column(name = "client_id")
    private String clientId;

    /**
     * app终端系统:android,ios
     */
    private String os;

    /**
     * app版本号
     */
    private String version;

    /**
     * app终端ip
     */
    private String ip;

    /**
     * 最后上传经度
     */
    private Double longitude;

    /**
     * 最后上传纬度
     */
    private Double latitude;

}