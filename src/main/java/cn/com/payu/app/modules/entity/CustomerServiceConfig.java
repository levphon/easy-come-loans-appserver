package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_customer_service_config")
public class CustomerServiceConfig extends BaseEntity {

    /**
     * QQ客服
     */
    @Column(name = "qq_number")
    private String qqNumber;

    /**
     * 在线客服
     */
    @Column(name = "online_link")
    private String onlineLink;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 工作时间
     */
    @Column(name = "work_time")
    private String workTime;

}