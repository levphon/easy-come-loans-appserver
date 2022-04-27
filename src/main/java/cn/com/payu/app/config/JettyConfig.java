package cn.com.payu.app.config;

import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : lqf
 * @description : jetty自动配置
 * @date : Create in 14:58 2018/5/8
 */
@Configuration
public class JettyConfig {

    @Bean
    public ServletWebServerFactory servletContainer() {
        JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
        factory.addServerCustomizers(jettyServerCustomizer());
        return factory;
    }

    @Bean
    public JettyServerCustomizer jettyServerCustomizer() {
        return server -> {
            threadPool(server);
//            accessLog(server);
        };
    }

    private void threadPool(Server server) {
        // Tweak the connection config used by Jetty to handle incoming HTTP
        // connections
        final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
        //默认最大线程连接数200
        threadPool.setMaxThreads(100);
        //默认最小线程连接数8
        threadPool.setMinThreads(20);
        //默认线程最大空闲时间60000ms
        threadPool.setIdleTimeout(60000);
    }

    /**
     * jetty启动日志
     *
     * @param server
     */
    private void accessLog(Server server) {
        CustomRequestLog requestLog = new CustomRequestLog("/data/java_log/jetty/logs/jetty-yyyy_mm_dd.request.log");
        server.setRequestLog(requestLog);
    }
}

