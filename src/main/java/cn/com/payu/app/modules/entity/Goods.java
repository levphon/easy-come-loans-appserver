package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "t_goods")
public class Goods extends BaseEntity {

    private String name;

    /**
     * 商品代码
     */
    @Column(name = "goods_code")
    private String goodsCode;

    /**
     * 商品类型
     */
    @Column(name = "goods_type")
    private Integer goodsType;

    /**
     * 原价，单位元
     */
    @Column(name = "origin_price")
    private BigDecimal originPrice;

    /**
     * 价格，单位元
     */
    private BigDecimal price;

    /**
     * 成本价，单位元
     */
    @Column(name = "cost_price")
    private BigDecimal costPrice;


    /**
     * 备注
     */
    private String remark;

    /**
     * 商品图片
     */
    private String url;

    /**
     * 排序
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 商品关联id
     */
    @Column(name = "ref_id")
    private Long refId;

    /**
     * 状态（1=上架 2=下架）
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

}