package cn.com.payu.app.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "qiakr")
public class QiakrConfig {

    private String appId;

    private String channelId;

    private String publicKey;

    private String privateKey;

    private String aesKey;

    private String baseApiUrl;

}
