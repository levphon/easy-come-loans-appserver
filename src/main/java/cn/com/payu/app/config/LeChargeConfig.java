package cn.com.payu.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Auther: csp1999
 * @Date: 2020/11/13/19:19
 * @Description: 支付宝配置类(读取配置文件)
 */
@Data
@Configuration
@PropertySource("classpath:config/application-lecharge.properties")
@ConfigurationProperties(prefix = "lecharge")
public class LeChargeConfig {

    private String agentCode;
    private String secretKey;
    private String baseApiUrl;
    private String notifyUrl;

}
