package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_order_application")
public class OrderApplication extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "cuser_id")
    private Long cuserId;

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 进件状态
     */
    private Integer status;

}