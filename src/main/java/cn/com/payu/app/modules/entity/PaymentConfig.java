package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_payment_config")
public class PaymentConfig extends BaseEntity {

    /**
     * APPID
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * 名称
     */
    private String name;

    /**
     * 关联公司
     */
    private String company;

    /**
     * （今天）支付次数
     */
    @Column(name = "pay_cnt")
    private Integer payCnt;

    /**
     * 1有效
     */
    private Integer status;

}