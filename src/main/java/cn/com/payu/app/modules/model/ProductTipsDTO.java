package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class ProductTipsDTO {

    private Long id;
    private String name;
    private String logo;
    private String tags;
    private String url;
    private String tipsContent;

}
