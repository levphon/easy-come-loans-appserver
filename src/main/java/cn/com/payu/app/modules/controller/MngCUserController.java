package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.CustUserRegisterListDTO;
import cn.com.payu.app.modules.model.export.CustUserRegisterExport;
import cn.com.payu.app.modules.model.params.CustUserSearch;
import cn.com.payu.app.modules.service.MngCustUserService;
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
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/mng/cuser")
@Api(value = "后台C端用户管理模块", tags = {"后台C端用户管理模块"})
public class MngCUserController {


    @Autowired
    private MngCustUserService mngCustUserService;

    @ApiOperation("注册用户列表查询")
    @GetMapping("/search")
    public R registerSearch(CustUserSearch search) {
        PageInfo<CustUserRegisterListDTO> pageInfo = mngCustUserService.registerSearch(search);
        return R.ok().putPageData(pageInfo);
    }

    @ApiOperation("注册用户导出")
    @GetMapping(value = "/export")
    public void registerExport(HttpServletResponse response, CustUserSearch search) throws Exception {
        List<CustUserRegisterExport> list = mngCustUserService.registerExport(search);
        EasyExcelUtils.writeExcel(response, list, "注册用户_" + DateUtils.formatSerial(new Date()), "Sheet1", CustUserRegisterExport.class);
    }

}
