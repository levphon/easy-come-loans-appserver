package cn.com.payu.app.modules;

import cn.com.payu.app.modules.converter.UserConverter;
import cn.com.payu.app.modules.model.AppUser;
import cn.com.payu.app.modules.model.LoginByMobileBO;
import cn.com.payu.app.modules.service.LoginService;
import cn.com.payu.app.modules.service.UserService;
import cn.hutool.core.util.ObjectUtil;
import com.glsx.plat.core.web.R;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author payu
 */
@Slf4j
@RestController
@Api(value = "本地登录模块", tags = {"本地登录模块"})
public class LocalLoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    /**
     * 本地登录
     */
    @ApiOperation(value = "注册/登录接口")
    @PostMapping(value = "/login")
    public R login(@RequestBody @Validated LoginByMobileBO loginBO) {

        AppUser user = loginService.localAuth(loginBO);

        String token = userService.createToken(user);

        Map<String, String> userMap = Maps.newHashMap();
        userMap.put("account", user.getAccount());

        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("token", token);
        rtnMap.put("user", userMap);
        //user给前端判断是否已经完善资料
        if (ObjectUtil.isNotNull(user.getProfileId())) {
            rtnMap.put("profile", UserConverter.INSTANCE.do2dto(user));
        }
        return R.ok().data(rtnMap);
    }

    /**
     * 本地登录
     */
    @ApiOperation(value = "联登接口")
    @PostMapping(value = "/ulogin")
    public R uLogin(@RequestBody @Validated LoginByMobileBO loginBO) {
        log.info("联登免登陆参数：{}", loginBO.toString());
        //参数检验
        if (loginBO.getLoginType() != 3) {
            return R.ok("联登参数异常，跳转到登录页面");
        }

        AppUser user = loginService.uLogin(loginBO);
        if (user == null) {
            return R.ok("联登失败，跳转到登录页面");
        }

        String token = userService.createToken(user);

        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("token", token);
        //user给前端判断是否已经完善资料
        if (ObjectUtil.isNotNull(user.getProfileId())) {
            rtnMap.put("user", UserConverter.INSTANCE.do2dto(user));
        } else {
            Map<String, String> userMap = Maps.newHashMap();
            userMap.put("account", user.getAccount());
            rtnMap.put("user", userMap);
        }
        return R.ok().data(rtnMap);
    }

    @ApiOperation(value = "登出接口")
    @GetMapping(value = "/logout")
    public R logout() {
        //todo 拉黑token
        return R.ok();
    }

    @ApiOperation(value = "注销账号接口")
    @PostMapping(value = "/destroyAccount")
    public R destroyAccount() {
        userService.destroyAccount();
        return R.ok();
    }

}
