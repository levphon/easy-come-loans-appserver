package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.ApplySummaryDTO;
import cn.com.payu.app.modules.model.ClickSummaryDTO;
import cn.com.payu.app.modules.model.RegisterSummaryDTO;
import cn.com.payu.app.modules.model.params.SummarySearch;
import cn.com.payu.app.modules.service.HomeService;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/mng/home")
@Api(value = "首页", tags = {"首页"})
public class MngHomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping(value = "/click/summary")
    public R clickSummary(SummarySearch search) {
        ClickSummaryDTO summaryDTO = homeService.clickSummary(search);
        return R.ok().data(summaryDTO);
    }

    @GetMapping(value = "/register/summary")
    public R registerSummary(SummarySearch search) {
        RegisterSummaryDTO summaryDTO = homeService.registerSummary(search);
        return R.ok().data(summaryDTO);
    }

    @GetMapping(value = "/apply/summary")
    public R applySummary(SummarySearch search) {
        ApplySummaryDTO summaryDTO = homeService.applySummary(search);
        return R.ok().data(summaryDTO);
    }

}

