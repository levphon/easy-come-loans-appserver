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
@PropertySource("classpath:config/application-danmi.properties")
@ConfigurationProperties(prefix = "danmi")
public class DanmiConfig {

    /**
     * ACCOUNT ID
     */
    private String accountId;

    /**
     * ACCOUNT SID
     */
    private String accountSId;

    /**
     * AUTH TOKEN
     */
    private String authToken;

    /**
     * API 地址
     */
    private String apiSendSMS;

    /**
     * 响应数据类型, JSON或XML
     */
    public static final String RESP_DATA_TYPE = "JSON";

    /**
     * 验证码模板id
     */
    private String templateId;

}
