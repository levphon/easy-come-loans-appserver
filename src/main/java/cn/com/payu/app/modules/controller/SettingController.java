package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.UserFeedbackBO;
import cn.com.payu.app.modules.service.SettingService;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/setting")
@Api(value = "系统设置模块(反馈、销户等)", tags = {"设置模块"})
public class SettingController {

    @Autowired
    private SettingService settingService;

    @ApiOperation(value = "系统反馈接口")
    @PostMapping(value = "/feedback")
    public R feedback(@RequestBody UserFeedbackBO feedbackBO) {
        settingService.feedback(feedbackBO);
        return R.ok();
    }

    @ApiOperation(value = "注销账号接口")
    @PostMapping(value = "/destroyAccount")
    public R destroyAccount() {
        settingService.destroyAccount();
        return R.ok();
    }

}
