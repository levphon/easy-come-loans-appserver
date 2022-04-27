package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_cust_user")
public class User extends BaseEntity {

    /**
     * 登录token
     */
    private String token;

    /**
     * token过期时间
     */
    @Column(name = "expire_in")
    private Long expireIn;

    /**
     * 登录失败次数
     */
    @Column(name = "try_times")
    private Integer tryTimes;

    /**
     * 用户账号/手机号
     */
    private String account;

    /**
     * 注册渠道：0默认，1邀请注册
     */
    private String channel;

    /**
     * 黑名单状态：0白名单，1临时封禁，2永久封禁
     */
    private Integer blacklist;

    /**
     * 来自app：1：默认橙色 2：绿色 3：黑色 4：蓝色
     */
    @Column(name = "from_app")
    private Integer fromApp;

}