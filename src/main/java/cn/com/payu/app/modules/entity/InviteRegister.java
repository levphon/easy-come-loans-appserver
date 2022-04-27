package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_cust_user_invite_register")
public class InviteRegister extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 邀请注册用户id
     */
    @Column(name = "register_id")
    private Long registerId;

    /**
     * 状态：0留存号码，1注册完成，2认证完成
     */
    private Integer status;

}