package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String account;
    private String avatar;
    private String unionId;
    private Integer blacklist;

}
