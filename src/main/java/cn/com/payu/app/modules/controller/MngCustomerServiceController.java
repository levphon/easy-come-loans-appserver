package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.CustomerServiceConfigDTO;
import cn.com.payu.app.modules.model.params.MngCustomerServiceConfigBO;
import cn.com.payu.app.modules.service.MngCustomerService;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/mng/customerservice")
@Api(value = "后台客服管理模块", tags = {"后台客服管理模块"})
public class MngCustomerServiceController {

    @Autowired
    private MngCustomerService mngCustomerService;

    /**
     * 获取客服配置
     */
    @GetMapping(value = "/getConfig")
    public R getConfig() {
        CustomerServiceConfigDTO configDTO = mngCustomerService.getConfig();
        return R.ok().data(configDTO);
    }

    /**
     * 设置客服配置
     */
    @PostMapping(value = "/config")
    public R config(@RequestBody MngCustomerServiceConfigBO configBO) {
        mngCustomerService.setConfig(configBO);
        return R.ok();
    }

}
