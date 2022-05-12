package cn.com.payu.app.modules.controller;

import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/home")
@Api(value = "首页", tags = {"首页"})
public class HomeController {

    @GetMapping(value = "/click/summary")
    public R clickSummary() {

        return R.ok();
    }

    @GetMapping(value = "/register/summary")
    public R registerSummary() {

        return R.ok();
    }

    @GetMapping(value = "/apply/summary")
    public R applySummary() {

        return R.ok();
    }

}

