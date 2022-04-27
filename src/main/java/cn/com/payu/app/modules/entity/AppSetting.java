package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "t_app_setting")
public class AppSetting extends BaseEntity {

    /**
     * 设置类型：notify,privacy
     */
    private String type;

    /**
     * 设置名称
     */
    private String title;

    /**
     * 设置项：post_liked、post_commented、followed、privacy_visit
     */
    private String item;

    /**
     * 设置项默认值
     */
    @Column(name = "item_val")
    private Integer itemValue;

}