package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.constant.Constants;
import cn.com.payu.app.common.enums.AliyunSMSTemplate;
import cn.com.payu.app.config.DanmiConfig;
import cn.com.payu.app.modules.entity.User;
import cn.com.payu.app.modules.mapper.UserMapper;
import cn.com.payu.app.modules.utils.DanmiHttpUtil;
import com.aliyuncs.CommonResponse;
import com.glsx.plat.common.utils.DateUtils;
import com.glsx.plat.common.utils.StringUtils;
import com.glsx.plat.exception.BusinessException;
import com.glsx.plat.redis.utils.RedisUtils;
import com.glsx.plat.sms.utils.AliyunSmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author liuyf
 * @Description 公共Service
 * @date 2019年10月22日 下午3:01:25
 */
@Slf4j
@Service
public class SmsService {

    @Value("${spring.application.name}")
    private String application;

    @Value("${sms.switch:on}")
    private String smsSwitch;

    @Value("${sms.daily.limit:0}")
    private Integer smsDailyLimit;

    @Value("${sms.default.code:111111}")
    private String smsDefaultCode;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private AliyunSmsUtils aliyunSmsUtils;

    @Resource
    private UserMapper userMapper;

//    @Resource
//    private MessageService messageService;

    @Autowired
    private DanmiConfig danmiConfig;

    /**
     * 发送验证码
     *
     * @param mobile
     */
    public String sendCode(String mobile) {
        //校验发送
        checkSend(mobile);

        String phoneKey = application + "." + mobile;
        String code = StringUtils.generateRandomCode(true, 6);
        if ("on".equalsIgnoreCase(smsSwitch)) {
            String smsKey = Constants.SMS_VERIFY_CODE_PREFIX + mobile;

            //this.sendAliyunSms(mobile, code);
            this.sendDanmiSms(mobile, code);

            redisUtils.setex(smsKey, code, Constants.SMS_VERIFY_CODE_TIMEOUT);
            code = "";
        } else {
            code = smsDefaultCode;
        }
        //设置每日发送次数
        setRedisDailyCnt(phoneKey);
        return code;
    }

    /**
     * 阿里云短信服务
     *
     * @param mobile
     * @param code
     */
    public void sendAliyunSms(String mobile, String code) {
        CommonResponse response = aliyunSmsUtils.sendSms(mobile, AliyunSMSTemplate.LOGIN_CONFIRM.getType(), "{\"code\":\"" + code + "\"}");
        if (response != null) {
            log.info(response.getData());
        }
    }

    /**
     * 短信发送(验证码通知，会员营销)
     * 接口文档地址：http://www.danmi.com/developer.html#smsSend
     *
     * @param mobile
     * @param code
     */
    public void sendDanmiSms(String mobile, String code) {
        StringBuilder sb = new StringBuilder();
        sb.append("accountSid").append("=").append(danmiConfig.getAccountSId());
        sb.append("&to").append("=").append(mobile);
        try {
            sb.append("&param").append("=").append(URLEncoder.encode(code, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sb.append("&templateid").append("=").append(danmiConfig.getTemplateId());
        String body = sb + DanmiHttpUtil.createCommonParam(danmiConfig.getAccountSId(), danmiConfig.getAuthToken());
        String result = DanmiHttpUtil.post(danmiConfig.getApiSendSMS(), body);
        log.info("Sms send result:{}", result);
    }

    public void checkSend(String mobile) {
        //1、黑名单用户无法发短信
        User user = userMapper.selectByAccount(mobile);
        boolean blacklist = user != null && user.getBlacklist() == 2;
        if (blacklist) {
            throw BusinessException.create("发送失败，请联系客服人员");
        }

        String phoneKey = application + "." + mobile;
        //2、每日发送次数上限
        int dailyCnt = getRedisDailyCnt(phoneKey);
        log.info("{} 今日第{}次发送，每日限额：{}", phoneKey, dailyCnt + 1, smsDailyLimit);
        if (dailyCnt >= smsDailyLimit) {
            throw BusinessException.create("今日发送次数超过限制");
        }

        //3、过快点击，如反复刷新页面或重新进去app，前端发送倒计时失效等
        String repeat = "repeat";
        if (redisUtils.hasKey(phoneKey + repeat)) {
            throw BusinessException.create("发送频率过快，请等待60秒后重试");
        }
        redisUtils.setex(phoneKey + repeat, repeat, Constants.SMS_VERIFY_CODE_REPEAT_TIMEOUT);
    }

    /**
     * 验证验证码
     *
     * @param phone
     * @param code
     */
    public void verifyCode(String phone, String code) {
        if ("on".equalsIgnoreCase(smsSwitch)) {
            String phoneKey = Constants.SMS_VERIFY_CODE_PREFIX + phone;
            String realVerifyCode = (String) redisUtils.get(phoneKey);
            if (StringUtils.isBlank(realVerifyCode) || !realVerifyCode.equals(code))
                throw BusinessException.create("验证码不正确");
        }
    }

    /**
     * 获取每天缓存次数
     *
     * @param key
     * @return
     */
    public int getRedisDailyCnt(String key) {
        Integer cnt = (Integer) redisUtils.get(key);
        if (cnt == null) cnt = 0;
        return cnt;
    }

    /**
     * 设置每天缓存次数
     *
     * @param key
     * @return
     */
    public int setRedisDailyCnt(String key) {
        Integer cnt = getRedisDailyCnt(key);
        int seconds = DateUtils.oddSecondOfDay();
        redisUtils.setex(key, ++cnt, seconds);
        return cnt;
    }

}
