package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class CustomerServiceConfigDTO {

    private Long id;
    private String qqNumber;
    private String onlineLink;
    private String email;
    private String workTime;

}
