package cn.com.payu.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sensetime")
public class SenseTimeConfig {

    private String keyId;

    private String keySecret;

    private String baseApiUrl;

}
