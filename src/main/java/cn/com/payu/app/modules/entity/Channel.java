package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "t_channel")
public class Channel extends BaseEntity {

    /**
     * 渠道
     */
    private String channel;

    /**
     * 渠道名称
     */
    @Column(name = "channel_name")
    private String channelName;

    /**
     * 打折率
     */
    @Column(name = "discount_rate")
    private Float discountRate;

    /**
     * 单价
     */
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（1=上架 2=下架）
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

}