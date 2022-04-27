package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class UserFeedbackBO {

    private Long feedback;
    private String mobile;
    private String content;
    private String images;

}
