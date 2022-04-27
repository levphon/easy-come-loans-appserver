package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_order_address")
public class OrderAddress extends BaseEntity {

    @Column(name = "order_id")
    private Long orderId;

    private String name;

    private String tel;

    private String address;

    @Column(name = "express_no")
    private String expressNo;

}