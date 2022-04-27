package cn.com.payu.app.modules.model.params;

import cn.com.payu.app.modules.model.PrepayBO;
import lombok.Data;

@Data
public class BuyGoodsBO extends PrepayBO {

    private String name;

    private String phone;

    private String address;

}
