package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class VipDTO {

    private Long id;
    /**
     * 期限
     */
    private Integer during;
    /**
     * 期限单位：1日，2月，3年
     */
    private Integer duringType;
    /**
     * 价格
     */
    private Integer price;
    /**
     * 估值
     */
    private String worth;

    /**
     * 排序
     */
    private Integer orderNum;

}
