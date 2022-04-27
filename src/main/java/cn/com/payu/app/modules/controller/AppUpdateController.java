package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.AppUpdateDTO;
import cn.com.payu.app.modules.service.AppUpdateService;
import com.glsx.plat.common.annotation.NoLogin;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(value = "应用更新模块", tags = {"应用更新模块"})
public class AppUpdateController {

    @Autowired
    private AppUpdateService appUpdateService;

    @NoLogin
    @ApiOperation(value = "应用更新接口")
    @GetMapping(value = "/update")
    public R update() {
        AppUpdateDTO appUpdateDTO = appUpdateService.info("xinxinrong");
        return R.ok().data(appUpdateDTO);
    }

}
