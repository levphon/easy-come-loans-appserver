package cn.com.payu.app.modules.controller;

import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.model.RoleDTO;
import cn.com.payu.app.modules.model.SimpleRoleDTO;
import cn.com.payu.app.modules.model.params.RoleBO;
import cn.com.payu.app.modules.model.params.RoleSearch;
import cn.com.payu.app.modules.service.MngRoleService;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.common.enums.OperateType;
import com.glsx.plat.context.utils.validator.AssertUtils;
import com.glsx.plat.core.web.R;
import com.glsx.plat.exception.SystemMessage;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/mng/role")
@Api(value = "后台角色管理模块", tags = {"后台角色管理模块"})
public class MngRoleController {

    private final static String MODULE = "角色管理";

    private final Long adminRoleId = 1L;

    @Autowired
    private MngRoleService roleService;

    @GetMapping("/search")
    public R search(RoleSearch search) {
        PageInfo<RoleDTO> pageInfo = roleService.search(search);
        return R.ok().putPageData(pageInfo);
    }

    @GetMapping("/simplelist")
    public R simplelist(@RequestParam(required = false) Long userId) {
        List<SimpleRoleDTO> list = roleService.simpleList(userId);
        return R.ok().data(list);
    }


    @SysLog(module = MODULE, action = OperateType.ADD)
    @PostMapping("/add")
    public R add(@RequestBody @Validated RoleBO roleBO) {
        roleService.addRole(roleBO);
        return R.ok();
    }

    @SysLog(module = MODULE, action = OperateType.EDIT)
    @PostMapping("/edit")
    public R edit(@RequestBody @Validated RoleBO roleBO) {
        AssertUtils.isNull(roleBO.getRoleId(), "ID不能为空");
        if (roleBO.getRoleId().equals(adminRoleId)) {
            throw AppServerException.of(SystemMessage.OPERATE_PERMISSION_DENIED);
        }
        roleService.editRole(roleBO);
        return R.ok();
    }

    @GetMapping("/info")
    public R info(@RequestParam("id") Long id) {

        RoleDTO roleDTO = roleService.roleInfo(id);

        return R.ok().data(roleDTO);
    }

    //    @RequireFunctionPermissions(permissionType = FunctionPermissionType.ROLE_DELETE)
    @SysLog(module = MODULE, action = OperateType.DELETE)
    @GetMapping("/delete")
    public R delete(@RequestParam("id") Long id) {
        if (id.equals(adminRoleId)) {
            throw AppServerException.of(SystemMessage.OPERATE_PERMISSION_DENIED);
        }
        roleService.deleteRole(id);
        return R.ok();
    }

}
