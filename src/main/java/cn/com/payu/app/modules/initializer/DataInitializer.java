package cn.com.payu.app.modules.initializer;

import com.glsx.plat.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private RedisUtils redisUtils;

    @PostConstruct
    public void init() {

    }
    
}
