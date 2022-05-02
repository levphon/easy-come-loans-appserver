package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "t_product")
public class Product extends BaseEntity {

    /**
     * 产品标识
     */
    private String channel;

    /**
     * 产品名称
     */
    private String name;

    private String logo;

    private String tags;

    private String city;

    @Column(name = "city_code")
    private String cityCode;

    /**
     * 最大额度
     */
    @Column(name = "max_amount")
    private BigDecimal maxAmount;

    /**
     * 日利率
     */
    @Column(name = "day_interest_rate")
    private Float dayInterestRate;

    /**
     * 期数
     */
    private Integer periods;

    /**
     * 使用人数
     */
    @Column(name = "used_cnt")
    private Integer usedCnt;

    /**
     * 单价
     */
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    /**
     * 渠道网址
     */
    private String url;

    /**
     * 产品类型：0普通产品，1会员产品
     */
    private Integer type;

    /**
     * 扩展信息
     */
    private String remark;

    /**
     * 排序
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 状态（1=上架 2=下架）
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

    /**
     * 上架位置
     */
    private Integer position;

    /**
     * 上架时间
     */
    @Column(name = "shelf_time")
    private String shelfTime;

    /**
     * 下架时间
     */
    @Column(name = "off_shelf_time")
    private String offShelfTime;

    /**
     * UV每天最大值
     */
    @Column(name = "max_daily_uv")
    private Integer maxDailyUV;

    /**
     * UV总量最大值
     */
    @Column(name = "max_total_uv")
    private Integer maxTotalUV;

}