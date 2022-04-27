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

//    @SysLog(module = MODULE, action = OperateType.ADD)
//    @ApiOperation("新增支付")
//    @PostMapping(value = "/add")
//    public R add(@RequestBody @Validated MngPaymentBO paymentBO) {
//        mngPaymentService.add(paymentBO);
//        return R.ok();
//    }
//
//    @GetMapping("/info")
//    public R info(@RequestParam("id") Long id) {
//        MngGoodsInfoDTO infoDTO = mngPaymentService.getInfoById(id);
//        return R.ok().data(infoDTO);
//    }
//
//    @SysLog(module = MODULE, action = OperateType.EDIT)
//    @ApiOperation("编辑支付")
//    @PostMapping(value = "/edit")
//    public R edit(@RequestBody @Validated MngPaymentBO paymentBO) {
//        AssertUtils.isNull(paymentBO.getId(), "ID不能为空");
//        mngPaymentService.edit(paymentBO);
//        return R.ok();
//    }

    @ApiOperation("切换支付")
    @SysLog(module = MODULE, action = OperateType.SETTING)
    @GetMapping("/switch")
    public R switchPayment(@RequestParam("id") Long id) {
        mngPaymentService.switchPayment(id);
        return R.ok();
    }

}
