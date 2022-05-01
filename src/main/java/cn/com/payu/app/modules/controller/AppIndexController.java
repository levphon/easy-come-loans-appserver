package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.UserLoanDTO;
import cn.com.payu.app.modules.model.UserProfileDTO;
import cn.com.payu.app.modules.model.params.UserLoanBO;
import cn.com.payu.app.modules.service.UserService;
import cn.com.payu.app.modules.utils.AppContextHolder;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.common.enums.OperateType;
import com.glsx.plat.core.web.R;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/index")
@Api(value = "App首页", tags = {"App首页"})
public class AppIndexController {

    private final static String MODULE = "App首页";

    @Autowired
    private UserService userService;

    @SysLog(module = MODULE, action = OperateType.SETTING)
    @PostMapping(value = "/loan")
    public R loan(@RequestBody @Validated UserLoanBO loanBO) {
        userService.setUserLoan(loanBO);
        return R.ok();
    }

    @GetMapping(value = "/getLoan")
    public R getLoan() {
        UserLoanDTO loanDTO = userService.getUserLoan(AppContextHolder.getUserId());
        return R.ok().data(loanDTO);
    }

    @GetMapping(value = "/index")
    public R isCertificated() {
        UserProfileDTO profile = userService.getUserProfileInfo(AppContextHolder.getUserId());
        Integer status = 0;//待认证
        if (profile != null) {
            status = 1;//待审核
        }

        Map<String, Object> rtnMap = Maps.newHashMap();
        rtnMap.put("profile", profile);
        rtnMap.put("status", status);
        return R.ok().data(rtnMap);
    }

}
