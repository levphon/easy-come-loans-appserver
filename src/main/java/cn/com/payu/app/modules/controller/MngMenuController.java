package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.MenuDTO;
import cn.com.payu.app.modules.model.MenuModel;
import cn.com.payu.app.modules.model.params.MenuBO;
import cn.com.payu.app.modules.model.params.MenuSearch;
import cn.com.payu.app.modules.service.MngMenuService;
import cn.com.payu.app.modules.utils.MngContextHolder;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.common.enums.OperateType;
import com.glsx.plat.context.utils.validator.AssertUtils;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(value = "/mng/menu")
@Api(value = "后台菜单管理模块", tags = {"后台菜单管理模块"})
public class MngMenuController {

    private final static String MODULE = "菜单管理";

    @Resource
    private MngMenuService menuService;

    @GetMapping("/search")
    public R search(MenuSearch search) {
        PageInfo<MenuDTO> pageInfo = menuService.search(search);
        return R.ok().putPageData(pageInfo);
    }

    @GetMapping("/children")
    public R children(@RequestParam("parentId") Long parentId) {
        List<MenuDTO> list = menuService.children(parentId);
        return R.ok().data(list);
    }

    /**
     * 用户登录加载系统资源
     *
     * @return
     */
    @GetMapping("/permtree")
    public R getMenuPermTree() {
        List<MenuModel> menuTree = menuService.getMenuTree(MngContextHolder.getRoleIds());
        return R.ok().data(menuTree);
    }

    /**
     * 子页面加载资源
     *
     * @return
     */
    @GetMapping("/subtree")
    public R getMenuSubtree(@RequestParam("parentId") Long parentId) {
        List<MenuModel> menuTree = menuService.getMenuTreeByParentId(parentId, MngContextHolder.getRoleIds());
        return R.ok().data(menuTree);
    }

    /**
     * 新增、编辑角色时加载菜单树
     *
     * @param roleId
     * @return
     */
    @GetMapping("/checkedtree")
    public R getMenuFullTree(@RequestParam(value = "roleId", required = false) Long roleId) {
        List<MenuModel> menuTree = menuService.getMenuFullTreeWithChecked(MngContextHolder.getRoleIds(), roleId);
        Set<Long> checkedIds = menuService.getMenuCheckedIds(roleId);

        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("menuTree", menuTree);
        rtnMap.put("checkedIds", checkedIds);
        return R.ok().data(rtnMap);
    }

    @SysLog(module = MODULE, action = OperateType.ADD)
    @PostMapping("/add")
    public R add(@RequestBody @Valid MenuBO menuBO) {
        menuService.add(menuBO);
        return R.ok();
    }

    @SysLog(module = MODULE, action = OperateType.EDIT)
    @PostMapping("/edit")
    public R edit(@RequestBody @Valid MenuBO menuBO) {
        AssertUtils.isNull(menuBO.getId(), "ID不能为空");
        menuService.edit(menuBO);
        return R.ok();
    }

    @GetMapping("/info")
    public R info(@RequestParam("menuId") Long id) {
        MenuDTO menuDTO = menuService.getMenuById(id);
        return R.ok().data(menuDTO);
    }

    @SysLog(module = MODULE, action = OperateType.DELETE)
    @GetMapping("/delete")
    public R delete(@RequestParam("menuId") Long id) {
        menuService.logicDeleteById(id);
        return R.ok();
    }

}
