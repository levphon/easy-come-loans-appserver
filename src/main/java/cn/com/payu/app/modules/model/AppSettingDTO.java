package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class AppSettingDTO {

    private Long id;
    private String type;
    private String title;
    private String item;
    private Integer itemValue;

}
