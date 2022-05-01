package cn.com.payu.app.modules.model.params;

import cn.hutool.db.Page;
import lombok.Data;

@Data
public class CustUserSearch extends Page {

    private String cDate;
    private String sDate;
    private String eDate;

    private String channel;

    private String term;

    private Integer status;

}
