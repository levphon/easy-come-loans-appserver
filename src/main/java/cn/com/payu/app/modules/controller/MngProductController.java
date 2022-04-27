package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.MngProductBO;
import cn.com.payu.app.modules.model.MngProductDTO;
import cn.com.payu.app.modules.model.MngProductInfoDTO;
import cn.com.payu.app.modules.model.MngProductSettingDTO;
import cn.com.payu.app.modules.model.export.MngProductExport;
import cn.com.payu.app.modules.model.params.MngProductSettingBO;
import cn.com.payu.app.modules.model.params.ProductSearch;
import cn.com.payu.app.modules.service.MngProductService;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.common.enums.OperateType;
import com.glsx.plat.common.utils.DateUtils;
import com.glsx.plat.context.utils.validator.AssertUtils;
import com.glsx.plat.core.web.R;
import com.glsx.plat.office.excel.EasyExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/mng/product")
@Api(value = "后台产品管理模块", tags = {"后台产品管理模块"})
public class MngProductController {

    private final static String MODULE = "产品管理";

    @Autowired
    private MngProductService mngProductService;

    @ApiOperation("产品列表查询")
    @GetMapping("/search")
    public R search(@Valid ProductSearch search) {
        PageInfo<MngProductDTO> pageInfo = mngProductService.search(search);
        return R.ok().putPageData(pageInfo);
    }

    @ApiOperation("产品列表导出")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, ProductSearch search) throws Exception {
        List<MngProductExport> list = mngProductService.export(search);
        EasyExcelUtils.writeExcel(response, list, "产品_" + DateUtils.formatSerial(new Date()), "Sheet1", MngProductExport.class);
    }

    @SysLog(module = MODULE, action = OperateType.ADD)
    @ApiOperation("新增产品")
    @PostMapping(value = "/add")
    public R add(@RequestBody @Validated MngProductBO productBO) {
        mngProductService.add(productBO);
        return R.ok();
    }

    @GetMapping("/info")
    public R info(@RequestParam("id") Long id) {
        MngProductInfoDTO infoDTO = mngProductService.getInfoById(id);
        return R.ok().data(infoDTO);
    }

    @SysLog(module = MODULE, action = OperateType.EDIT)
    @ApiOperation("编辑产品")
    @PostMapping(value = "/edit")
    public R edit(@RequestBody @Validated MngProductBO productBO) {
        AssertUtils.isNull(productBO.getId(), "ID不能为空");
        mngProductService.edit(productBO);
        return R.ok();
    }

    @ApiOperation("获取产品配置")
    @GetMapping(value = "/getsetting")
    public R getSetting(@RequestParam("productId") Long productId) {
        MngProductSettingDTO settingDTO = mngProductService.getSetting(productId);
        return R.ok().data(settingDTO);
    }

    @ApiOperation("产品配置")
    @PostMapping(value = "/setting")
    public R setting(@RequestBody MngProductSettingBO settingBO) {
        mngProductService.setting(settingBO);
        return R.ok();
    }

    @ApiOperation("产品上下架")
    @GetMapping("/uplow")
    public R uplow(@RequestParam("id") Long id) {
        int uplow = mngProductService.uplow(id);
        return R.ok().data(uplow);
    }

}
