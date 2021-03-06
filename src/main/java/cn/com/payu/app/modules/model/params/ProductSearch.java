package cn.com.payu.app.modules.model.params;

import cn.hutool.db.Page;
import lombok.Data;

@Data
public class ProductSearch extends Page {

    private String sDate;
    private String eDate;

    private String productName;
    private String provinceCode;
    private String cityCode;
    private String os;
    private Integer position;
    private Integer enableStatus;

}
