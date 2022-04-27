package cn.com.payu.app.modules.model.params;

import cn.hutool.db.Page;
import lombok.Data;

@Data
public class GoodsSearch extends Page {

    private String goodsName;
    private Integer goodsType;
    private String enableStatus;

}
