package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.MngPaymentDTO;
import cn.com.payu.app.modules.model.params.MngPaymentSearch;
import cn.com.payu.app.modules.service.MngPaymentService;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.common.enums.OperateType;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/mng/payment")
@Api(value = "后台支付管理模块", tags = {"后台支付管理模块"})
public class MngPaymentController {

    private final static String MODULE = "支付管理";

    @Autowired
    private MngPaymentService mngPaymentService;

    @ApiOperation("支付列表查询")
    @GetMapping("/search")
    public R search(MngPaymentSearch search) {
        PageInfo<MngPaymentDTO> pageInfo = mngPaymentService.search(search);
        return R.ok().putPageData(pageInfo);
    }

    @ApiOperation("切换支付")
    @SysLog(module = MODULE, action = OperateType.SETTING)
    @GetMapping("/switch")
    public R switchPayment(@RequestParam("id") Long id) {
        mngPaymentService.switchPayment(id);
        return R.ok();
    }

}
