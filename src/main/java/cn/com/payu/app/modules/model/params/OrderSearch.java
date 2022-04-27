package cn.com.payu.app.modules.model.params;

import cn.hutool.db.Page;
import lombok.Data;

@Data
public class OrderSearch extends Page {

    private String orderNo;

    /**
     * 订单类型
     *
     * @see cn.com.payu.app.common.enums.GoodsType
     */
    private Integer orderType;

    private Integer fromApp;

    private String channel;

    private String username;

    private String sDate;
    private String eDate;

}
