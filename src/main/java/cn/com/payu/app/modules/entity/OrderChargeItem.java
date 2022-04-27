package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_order_charge_item")
public class OrderChargeItem extends BaseEntity {

    @Column(name = "order_id")
    private Long orderId;

    /**
     * 充值账号，可能是手机号码、QQ号等
     */
    private String account;

    /**
     * 充值状态（看具体商品，正常与order.status一致，如果外部服务商商品，则同充值服务商他们一致）
     */
    private String status;

}