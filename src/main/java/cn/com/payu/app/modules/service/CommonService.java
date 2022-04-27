package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.entity.SysIdRecord;
import cn.com.payu.app.modules.mapper.SysIdRecordMapper;
import com.glsx.plat.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CommonService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SysIdRecordMapper sysIdRecordMapper;

    public String generateIdBySys(String sysId) {
        SysIdRecord record = sysIdRecordMapper.selectBySysId(sysId);
        return "";
    }

    /**
     * 获取随机数,此方法不支持16位以上的随机数，如果n>16 那么n会被默认为16
     *
     * @param n 随机数位数
     * @return 一串字符串的随机数
     */
    public String getRandom(int n) {
        if (n > 16) {
            n = 16;
        }
        String a = "";
        Random r = new Random();
        Long b = Math.abs(r.nextLong());
        r = null;
        a = b.toString().substring(0, n);
        return a;
    }

}
