package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class BlacklistUserDTO {

    private Long id;
    private Long blackedUserId;
    private String username;
    private String avatar;

}
