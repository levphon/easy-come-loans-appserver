package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.converter.UserConverter;
import cn.com.payu.app.modules.entity.User;
import cn.com.payu.app.modules.model.InviteDownloadBO;
import cn.com.payu.app.modules.model.InviteRegisterBO;
import cn.com.payu.app.modules.service.InviteRegisterService;
import cn.com.payu.app.modules.service.SmsService;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.context.utils.PropertiesUtils;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/invite")
@Api(value = "邀请注册", tags = {"邀请注册"})
public class InviteRegisterController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private InviteRegisterService inviteRegisterService;

    /**
     * 邀请注册
     */
    @SysLog
    @PostMapping(value = "/register")
    public R register(@RequestBody InviteRegisterBO registerBO) {
        //验证码校验
//        smsService.verifyCode(registerBO.getMobile(), registerBO.getCode());

        User user = inviteRegisterService.inviteRegister(registerBO);
        if (user != null) {
            return R.ok().put("user", UserConverter.INSTANCE.do2dto(user))
                    .put("url", PropertiesUtils.getProperty("app.download.url"));
        }
        return R.ok();
    }

    @SysLog
    @PostMapping(value = "/download")
    public R download(@RequestBody InviteDownloadBO downloadBO) {
//        User user = userService.checkExistUser(downloadBO.getMobile());
//        if (user == null) {
//            throw new AppServerException(downloadBO.getMobile() + "号码未注册，请先在邀请页面注册后下载");
//        }
        //下载计数
        inviteRegisterService.recordDownload(downloadBO);

        String url = PropertiesUtils.getProperty(downloadBO.getType() + ".download.url");
        return R.ok().data(url);
    }

}
