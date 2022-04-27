package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.entity.SniffUnionLogin;
import cn.com.payu.app.modules.entity.User;
import cn.com.payu.app.modules.mapper.SniffUnionLoginMapper;
import cn.com.payu.app.modules.mapper.UserMapper;
import cn.com.payu.app.modules.model.CollisionAttackBO;
import cn.com.payu.app.modules.model.MobileRegisterBO;
import cn.com.payu.app.modules.model.UnionLoginBO;
import cn.com.payu.app.modules.utils.EncryptUtil;
import cn.com.payu.app.modules.utils.LeChargeMD5;
import com.glsx.plat.context.utils.PropertiesUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class UnionLoginService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SniffUnionLoginMapper sniffUnionLoginMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    public Map<String, Object> sniff(CollisionAttackBO attackBO) {

        Map<String, Object> rtnMap = Maps.newHashMap();

        User user = userMapper.selectByPhoneMd5(attackBO.getPhoneMd5());

        Integer filterResult = 0;
        String filterReason = "";

        if (user != null) {
            filterResult = 0;
            filterReason = "老用户";
        } else {
            filterResult = 1;
            filterReason = "新用户";

            Date now = new Date();

            SniffUnionLogin sul = new SniffUnionLogin();
            sul.setChannel(attackBO.getChannelCode());
            sul.setPhoneMd5(attackBO.getPhoneMd5());
            sul.setCollisionCode(String.valueOf(filterResult));
            sul.setCollisionDesc(filterReason);
            sul.setCollisionDatetime(now);
            sul.setCreatedDate(now);
            sniffUnionLoginMapper.insert(sul);
        }

        rtnMap.put("filterResult", filterResult);
        rtnMap.put("filterReason", filterReason);

        return rtnMap;
    }

    public Map<String, Object> unionLogin(UnionLoginBO loginBO) {
        if (loginBO.getFromApp() == null) {
            loginBO.setFromApp(1);
        }
        User checkedUser = userService.checkExistUser(loginBO.getPhone(), loginBO.getFromApp());
        if (checkedUser == null) {
            MobileRegisterBO register = new MobileRegisterBO();
            register.setMobile(loginBO.getPhone());
            register.setPassword("123456");
            register.setChannel(loginBO.getChannelCode());
            register.setFromApp(loginBO.getFromApp());
            loginService.register(register);

            String phoneMd5 = new LeChargeMD5().calcMD5(loginBO.getPhone());
            SniffUnionLogin sul = sniffUnionLoginMapper.selectByPhoneMd5(phoneMd5);
            if (sul != null) {
                sul.setPhone(loginBO.getPhone());
                sul.setRegisterCode(loginBO.getChannelCode());
                sul.setRegisterDesc("联登注册");
                sul.setRegisterDatetime(new Date());
                sniffUnionLoginMapper.updateByPrimaryKey(sul);
            }
        } else {
            log.warn("用户{}已存在", loginBO.getPhone());
        }

        String url = PropertiesUtils.getProperty("app.unionlogin.url");

        StringBuilder sb = new StringBuilder(url);
        sb.append("channel=").append(loginBO.getChannelCode()).append("&");
        sb.append("emobile=").append(EncryptUtil.PBEEncrypt(loginBO.getPhone()));

        Map<String, Object> rtnMap = Maps.newHashMap();
        rtnMap.put("url", sb.toString());
        return rtnMap;
    }

}
