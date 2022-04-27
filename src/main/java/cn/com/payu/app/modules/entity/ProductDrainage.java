package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_product_drainage")
public class ProductDrainage extends BaseEntity {

    /**
     * 渠道标识
     */
    private String channel;

    /**
     * 产品id
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 1有效
     */
    private Integer status;

}