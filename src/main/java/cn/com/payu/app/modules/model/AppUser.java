package cn.com.payu.app.modules.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AppUser {

    private Long id;
    private String unionId;
    private String username;
    private String account;
    private String avatar;
    private Integer blacklist;
    private String channel;
    private Integer fromApp;

    private Boolean isVip;

    @JsonIgnore
    private Long profileId;

}
