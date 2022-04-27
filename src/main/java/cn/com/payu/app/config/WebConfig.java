package cn.com.payu.app.config;

import com.glsx.plat.context.properties.UploadProperties;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author payu
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UploadProperties uploadProperties;

    @Autowired
    private AppRequestAuthorizeInterceptor appRequestAuthorizeInterceptor;

    @Autowired
    private MngRequestAuthorizeInterceptor mngRequestAuthorizeInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        String path = uploadProperties.getBasePath();
        registry.addResourceHandler(uploadProperties.getFilepath() + "**").addResourceLocations("file://" + path);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> excludePathPatterns = Lists.newArrayList();
        excludePathPatterns.add("/static/**");//静态文件
        excludePathPatterns.add(uploadProperties.getFilepath() + "**");//图片
        excludePathPatterns.add("/update");
        excludePathPatterns.add("/common/sendCode");
        excludePathPatterns.add("/register");
        excludePathPatterns.add("/invite/register");
        excludePathPatterns.add("/login");
        excludePathPatterns.add("/ulogin");
        excludePathPatterns.add("/wechatAuth");
        excludePathPatterns.add("/wechatLogin");
        excludePathPatterns.add("/sniff");
        excludePathPatterns.add("/unionLogin");
        excludePathPatterns.add("/qqAuth");
        excludePathPatterns.add("/qqLogin");
        excludePathPatterns.add("/bind");
        excludePathPatterns.add("/xactuator/**");
        excludePathPatterns.add("/api/**");
        excludePathPatterns.add("/mng/captcha");
        excludePathPatterns.add("/mng/login");
        excludePathPatterns.add("/payment/callback/**");
        excludePathPatterns.add("/invite/download");

        excludePathPatterns.add("/swagger-resources/**");
        excludePathPatterns.add("/webjars/**");
        excludePathPatterns.add("/v2/**");
        excludePathPatterns.add("/swagger-ui.html/**");


        //1.加入的顺序就是拦截器执行的顺序，
        //2.按顺序执行所有拦截器的preHandle
        //3.所有的preHandle 执行完再执行全部postHandle 最后是postHandle
        registry.addInterceptor(appRequestAuthorizeInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathPatterns);

        registry.addInterceptor(mngRequestAuthorizeInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathPatterns);
    }

}
