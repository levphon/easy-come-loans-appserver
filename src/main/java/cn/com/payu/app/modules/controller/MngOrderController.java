package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.MngOrderDTO;
import cn.com.payu.app.modules.model.MngOrderDetailsDTO;
import cn.com.payu.app.modules.model.OrderSummaryDTO;
import cn.com.payu.app.modules.model.export.MngOrderExport;
import cn.com.payu.app.modules.model.params.OrderSearch;
import cn.com.payu.app.modules.service.OrderService;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.common.utils.DateUtils;
import com.glsx.plat.core.web.R;
import com.glsx.plat.office.excel.EasyExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/mng/order")
@Api(value = "后台订单管理模块", tags = {"后台订单管理模块"})
public class MngOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("订单列表查询")
    @GetMapping("/search")
    public R search(@Valid OrderSearch search) {
        PageInfo<MngOrderDTO> pageInfo = orderService.search(search);
        return R.ok().putPageData(pageInfo);
    }

    @ApiOperation("订单列表导出")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, OrderSearch search) throws Exception {
        List<MngOrderExport> list = orderService.export(search);
        EasyExcelUtils.writeExcel(response, list, "订单_" + DateUtils.formatSerial(new Date()), "Sheet1", MngOrderExport.class);
    }

    @GetMapping(value = "/info")
    public R info(@RequestParam("id") Long id) {
        MngOrderDetailsDTO item = orderService.getById(id);
        return R.ok().data(item);
    }

    @GetMapping(value = "/summary")
    public R summary() {
        OrderSummaryDTO item = orderService.getSummary();
        return R.ok().data(item);
    }

}
