package cn.com.payu.app.modules;

import cn.com.payu.app.modules.converter.UserConverter;
import cn.com.payu.app.modules.entity.UserAuthRel;
import cn.com.payu.app.modules.model.AppUser;
import cn.com.payu.app.modules.model.CollisionAttackBO;
import cn.com.payu.app.modules.model.UnionLoginBO;
import cn.com.payu.app.modules.model.WechatLoginBO;
import cn.com.payu.app.modules.model.params.BindMobileBO;
import cn.com.payu.app.modules.model.params.QQLoginBO;
import cn.com.payu.app.modules.service.LoginService;
import cn.com.payu.app.modules.service.UnionLoginService;
import cn.com.payu.app.modules.service.UserService;
import cn.com.payu.app.modules.utils.LeChargeMD5;
import cn.hutool.core.util.ObjectUtil;
import com.glsx.plat.common.annotation.NoLogin;
import com.glsx.plat.context.utils.PropertiesUtils;
import com.glsx.plat.core.web.R;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author liuyf
 * @Title UserController.java
 * @Package com.hflw.vasp.controller
 * @Description 微信登录
 * @date 2019年10月24日 下午2:02:54
 */
@Slf4j
@RestController
//@Api(value = "第三方登录模块", tags = {"第三方登录模块"})
public class ThirdLoginController {

    private final WxMpService wxMpService;

    @Resource
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UnionLoginService unionLoginService;

    public ThirdLoginController(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    @NoLogin
    @PostMapping(value = "/wechatAuth")
    public R wechatAuth(@RequestParam String code) throws WxErrorException {
//        WxMpOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);

        WxOAuth2AccessToken accessToken = new WxOAuth2AccessToken();
        accessToken.setOpenId("adgdgadsaaaaaaaaaaaaaaaaa");
        accessToken.setUnionId("bbbbbbbbbbbbbbbbbbbbbbbbb");
        accessToken.setAccessToken("etegsagdsagdfffffffffffffffffffffffffffffffffffffffffff");

        UserAuthRel authRel = loginService.wechatAuth(accessToken);

        Map<String, Object> rtnMap = new HashMap<>();

        Optional<Long> optUserId = Optional.ofNullable(authRel.getUserId());
        if (optUserId.isPresent()) {
            //如果用户id存在，理论上User不为空
            AppUser user = userService.getAppUserByUserId(optUserId.get());
            String token = userService.createToken(user);
            rtnMap.put("token", token);

            //user给前端判断是否已经完善资料
            if (ObjectUtil.isNotNull(user.getProfileId())) {
                rtnMap.put("user", UserConverter.INSTANCE.do2dto(user));
            }
        } else {
            //没有绑定用户
            rtnMap.put("authRelId", authRel.getId());
        }
        return R.ok().data(rtnMap);
    }

    @NoLogin
    @PostMapping(value = "/wechatLogin")
    public R wechatLogin(@RequestBody WechatLoginBO loginBO) {
        log.info("微信授权参数 {}", loginBO.toString());
        UserAuthRel wxAuthRel = loginService.wechatLogin(loginBO);

        Map<String, Object> rtnMap = thirdLogin(wxAuthRel);

        return R.ok().data(rtnMap);
    }

    @NoLogin
    @PostMapping(value = "/qqAuth")
    public R qqAuth(@RequestParam String code) {
        return R.ok();
    }

    @NoLogin
    @PostMapping(value = "/qqLogin")
    public R qqLogin(@RequestBody QQLoginBO loginBO) {
        log.info("QQ授权参数 {}", loginBO.toString());
        UserAuthRel qqAuthRel = loginService.qqLogin(loginBO);

        Map<String, Object> rtnMap = thirdLogin(qqAuthRel);

        return R.ok().data(rtnMap);
    }

    private Map<String, Object> thirdLogin(UserAuthRel authRel) {
        Map<String, Object> rtnMap = new HashMap<>();

        Optional<Long> optUserId = Optional.ofNullable(authRel.getUserId());
        if (optUserId.isPresent()) {
            //如果用户id存在，理论上User不为空
            AppUser user = userService.getAppUserByUserId(optUserId.get());
            String token = userService.createToken(user);
            rtnMap.put("token", token);

            //user给前端判断是否已经完善资料
            if (ObjectUtil.isNotNull(user.getProfileId())) {
                rtnMap.put("user", UserConverter.INSTANCE.do2dto(user));
            }
        } else {
            //没有绑定用户
            rtnMap.put("authRelId", authRel.getId());
        }
        return rtnMap;
    }

    /**
     * 绑定手机号码(账号)
     */
    @PostMapping(value = "/bind")
    public R bind(@RequestBody BindMobileBO bindMobileBO) {
//        smsService.verifyCode(bindMobileBO.getMobile(), bindMobileBO.getCode());

        AppUser user = loginService.bind(bindMobileBO);

        Map<String, Object> rtnMap = new HashMap<>();

        String token = userService.createToken(user);
        rtnMap.put("token", token);

        //user给前端判断是否已经完善资料
        if (ObjectUtil.isNotNull(user.getProfileId())) {
            rtnMap.put("user", UserConverter.INSTANCE.do2dto(user));
        }
        return R.ok().data(rtnMap);
    }


    @PostMapping(value = "/sniff")
    public R sniff(@RequestBody @Validated CollisionAttackBO attackBO) {
        log.info("撞库参数：{}", attackBO.toString());
        StringBuilder sb = new StringBuilder();
        String channelCode = PropertiesUtils.getProperty("ulogin.channelCode");
        if (channelCode.equalsIgnoreCase(attackBO.getChannelCode())) {
            sb.append("phoneMd5=").append(attackBO.getPhoneMd5()).append("&channelCode=").append(attackBO.getChannelCode());
            String checkSign = new LeChargeMD5().calcMD5(sb.toString());
            log.info("撞库签名 {}", attackBO.getSign());
            log.info("本地签名 {}", checkSign);
            if (checkSign.toLowerCase().equals(attackBO.getSign())) {
                Map<String, Object> sniffResult = unionLoginService.sniff(attackBO);
                return R.ok().put("status", 1).data(sniffResult);
            } else {
                Map<String, Object> rtnMap = Maps.newHashMap();
                rtnMap.put("filterResult", 0);
                rtnMap.put("filterReason", "签名不成功");
                return R.ok().put("status", "1").data(rtnMap);
            }
        } else {
            Map<String, Object> rtnMap = Maps.newHashMap();
            rtnMap.put("filterResult", 0);
            rtnMap.put("filterReason", "渠道码错误");
            return R.ok().put("status", "1").data(rtnMap);
        }
    }

    @PostMapping(value = "/unionLogin")
    public R unionLogin(@RequestBody @Validated UnionLoginBO loginBO) {
        log.info("联登参数：{}", loginBO.toString());
        String channelCode = PropertiesUtils.getProperty("ulogin.channelCode");
        if (channelCode.equalsIgnoreCase(loginBO.getChannelCode())) {
            StringBuilder sb = new StringBuilder();
            sb.append("phone=").append(loginBO.getPhone()).append("&channelCode=").append(loginBO.getChannelCode());
            String checkSign = new LeChargeMD5().calcMD5(sb.toString());
            log.info("联登签名 {}", loginBO.getSign());
            log.info("本地签名 {}", checkSign);
            if (checkSign.toLowerCase().equals(loginBO.getSign())) {
                Map<String, Object> loginResult = unionLoginService.unionLogin(loginBO);
                return R.ok().put("status", 1).data(loginResult);
            } else {
                Map<String, Object> rtnMap = Maps.newHashMap();
                return R.error("签名不成功").put("status", 0).data(rtnMap);
            }
        } else {
            Map<String, Object> rtnMap = Maps.newHashMap();
            rtnMap.put("filterResult", 0);
            rtnMap.put("filterReason", "渠道码错误");
            return R.ok().put("status", "1").data(rtnMap);
        }
    }

}
