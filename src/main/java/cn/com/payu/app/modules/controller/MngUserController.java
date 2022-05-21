package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.MngUserDTO;
import cn.com.payu.app.modules.model.params.MngUserBO;
import cn.com.payu.app.modules.model.params.MngUserSearch;
import cn.com.payu.app.modules.service.MngUserService;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.common.enums.OperateType;
import com.glsx.plat.common.model.DropOptions;
import com.glsx.plat.context.utils.validator.AssertUtils;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/mng/user")
@Api(value = "后台用户管理模块", tags = {"后台用户管理模块"})
public class MngUserController {

    private final static String MODULE = "用户管理";

    @Resource
    private MngUserService mngUserService;

    @GetMapping("/search")
    public R search(MngUserSearch search) {
        PageInfo<MngUserDTO> pageInfo = mngUserService.search(search);
        return R.ok().putPageData(pageInfo);
    }

    @SysLog(module = MODULE, action = OperateType.ADD, saveLog = false)
    @PostMapping(value = "/add")
    public R add(@RequestBody @Validated MngUserBO userBO) {
        AssertUtils.isNull(userBO.getPassword(), "密码不能为空");
        mngUserService.addUser(userBO);
        return R.ok();
    }

    @SysLog(module = MODULE, action = OperateType.EDIT, saveLog = false)
    @PostMapping(value = "/edit")
    public R edit(@RequestBody @Validated MngUserBO userBO) {
        AssertUtils.isNull(userBO.getId(), "ID不能为空");
        mngUserService.editUser(userBO);
        return R.ok();
    }

    @GetMapping(value = "/info")
    public R info(@RequestParam("id") Long id) {
        MngUserDTO user = mngUserService.userInfo(id);
        return R.ok().data(user);
    }

    @GetMapping("/options")
    public R options(String username) {
        List<DropOptions> list = mngUserService.options(username);
        return R.ok().data(list);
    }

    @SysLog(module = MODULE, action = OperateType.DELETE, saveLog = false)
    @GetMapping(value = "/delete")
    public R delete(@RequestParam("id") Long id) {
        mngUserService.logicDeleteById(id);
        return R.ok();
    }

}
