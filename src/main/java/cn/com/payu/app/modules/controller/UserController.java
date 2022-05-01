package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.converter.UserConverter;
import cn.com.payu.app.modules.model.*;
import cn.com.payu.app.modules.model.params.BindBankCardBO;
import cn.com.payu.app.modules.service.UserService;
import cn.com.payu.app.modules.utils.AppContextHolder;
import cn.hutool.json.JSONUtil;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/user")
@Api(value = "用户模块", tags = {"用户模块"})
public class UserController {

    @Autowired
    private UserService userService;

    @SysLog
    @ApiOperation(value = "资料提交接口")
    @PostMapping(value = "/fillProfile")
    public R fillProfile(@RequestBody @Validated UserProfileBO userProfileBO) {
        log.info("资料提交：{}", JSONUtil.toJsonStr(userProfileBO));
        AppUser user = userService.fillProfile(userProfileBO);

        UserDTO userDTO = UserConverter.INSTANCE.do2dto(user);
        return R.ok().data(userDTO);
    }

    @GetMapping(value = "/info")
    public R info() {
        IndividualPage individualPage = userService.individualPage(AppContextHolder.getUserId());
        return R.ok().data(individualPage);
    }

    @ApiOperation(value = "查询用户是否认证接口")
    @GetMapping(value = "/iscertificated")
    public R isCertificated() {
        UserProfileDTO profile = userService.getUserProfileInfo(AppContextHolder.getUserId());
        Integer status = 0;//待认证
        if (profile != null) {
            status = 1;//待审核

            ZonedDateTime nowTime = ZonedDateTime.now();
            if (true) {

            } else {
                ZonedDateTime beginTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(profile.getRegisterDate().toInstant().toEpochMilli()), ZoneId.systemDefault());
                Duration duration = Duration.between(nowTime, beginTime).abs();
                if (duration.toHours() > 3) {
                    status = 2;//非会员超3小时
                }
            }
        }
        return R.ok().data(profile != null).put("profile", profile).put("status", status);
    }

    /**
     * 用户绑卡
     */
    @PostMapping(value = "/bankcard/bind")
    public R bindBankCard(@RequestBody BindBankCardBO bindBO) {
        userService.bindBankCard(bindBO);
        return R.ok();
    }

    /**
     * 获取用户绑卡记录
     */
    @GetMapping(value = "/bankcard/list")
    public R bankCardList() {
        List<BindBankCardDTO> list = userService.getBindBankCards();
        return R.ok().data(list);
    }

}
