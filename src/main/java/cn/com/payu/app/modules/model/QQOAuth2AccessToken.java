package cn.com.payu.app.modules.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class QQOAuth2AccessToken implements Serializable {

    private static final long serialVersionUID = -1345910558078620808L;

    private String accessToken;

    private int expiresIn = -1;

    private String refreshToken;

    private String openId;

    private String unionId;

}
