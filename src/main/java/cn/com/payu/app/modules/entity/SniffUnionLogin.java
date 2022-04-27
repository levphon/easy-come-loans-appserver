package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_sniff_union_login")
public class SniffUnionLogin extends BaseEntity {

    /**
     * 渠道标识
     */
    private String channel;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 手机号MD5加密串
     */
    @Column(name = "phone_md5")
    private String phoneMd5;

    /**
     * 撞库结果码
     */
    @Column(name = "collision_code")
    private String collisionCode;

    /**
     * 撞库结果说明
     */
    @Column(name = "collision_desc")
    private String collisionDesc;

    /**
     * 撞库返回时间戳
     */
    @Column(name = "collision_datetime")
    private Date collisionDatetime;

    /**
     * 注册结果码
     */
    @Column(name = "register_code")
    private String registerCode;

    /**
     * 注册结果说明
     */
    @Column(name = "register_desc")
    private String registerDesc;

    /**
     * 注册返回时间戳
     */
    @Column(name = "register_datetime")
    private Date registerDatetime;

}