package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_cust_user_setting")
public class UserSetting extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 设置id
     */
    @Column(name = "setting_id")
    private Long settingId;

    /**
     * 设置项
     */
    @Column(name = "item_val")
    private Integer itemValue;

}