package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.PageViewBO;
import cn.com.payu.app.modules.service.MinePointService;
import com.glsx.plat.common.annotation.SysLog;
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
@RequestMapping(value = "/minepoint")
@Api(value = "埋点统计模块", tags = {"埋点统计模块"})
public class MinePointController {

    @Autowired
    private MinePointService minePointService;

    @SysLog
    @ApiOperation(value = "PV/UV统计接口")
    @PostMapping(value = "/upv")
    public R upv(@RequestBody PageViewBO pageViewBO) {
        minePointService.upv(pageViewBO);
        return R.ok();
    }

}
