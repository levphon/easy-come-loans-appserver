package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.converter.RoleConverter;
import cn.com.payu.app.modules.entity.Role;
import cn.com.payu.app.modules.entity.RoleMenu;
import cn.com.payu.app.modules.entity.UserRoleRelation;
import cn.com.payu.app.modules.mapper.RoleMapper;
import cn.com.payu.app.modules.mapper.RoleMenuMapper;
import cn.com.payu.app.modules.mapper.UserRoleRelationMapper;
import cn.com.payu.app.modules.model.RoleDTO;
import cn.com.payu.app.modules.model.SimpleRoleDTO;
import cn.com.payu.app.modules.model.params.RoleBO;
import cn.com.payu.app.modules.model.params.RoleSearch;
import cn.com.payu.app.modules.utils.MngContextHolder;
import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.exception.SystemMessage;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: taoyr
 **/
@Slf4j
@Service
public class MngRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleRelationMapper userRoleRelationMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    public PageInfo<RoleDTO> search(RoleSearch search) {
        Page page = PageHelper.startPage(search.getPageNumber(), search.getPageSize());

        List<Role> list = roleMapper.selectList(search);

        List<RoleDTO> roleDTOList = getRoleListAssembled(list);

        PageInfo<RoleDTO> pageInfo = new PageInfo<>(roleDTOList);

        pageInfo.setPages(page.getPages());//总页数
        pageInfo.setTotal(page.getTotal());//总条数
        return pageInfo;
    }

    private List<RoleDTO> getRoleListAssembled(List<Role> list) {
        List<RoleDTO> roleDtoList = list.stream().map(role -> {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(role, roleDTO);
            return roleDTO;
        }).collect(Collectors.toList());

        return roleDtoList;
    }

    public List<Role> getUserRoleList(Long userId) {
        UserRoleRelation userRoleRelation = new UserRoleRelation();
        userRoleRelation.setUserId(userId);
        List<UserRoleRelation> userRoleRelationList = userRoleRelationMapper.selectUserRoleRelationList(userRoleRelation);

        List<Long> roleIds = userRoleRelationList.stream().map(UserRoleRelation::getRoleId).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }

        List<Role> roleList = roleMapper.selectByIds(roleIds);

        return roleList;
    }

    public Role getRoleById(Long roleId) {
        return roleMapper.selectById(roleId);
    }

    /**
     * 根据角色权限范围获取角色列表
     *
     * @return
     */
    public List<SimpleRoleDTO> simpleList(Long userId) {

        List<Role> roleList = roleMapper.selectList(new RoleSearch());

        List<SimpleRoleDTO> list = roleList.stream().map(role -> new SimpleRoleDTO().setRoleId(role.getId()).setRoleName(role.getRoleName())).collect(Collectors.toList());

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addRole(RoleBO roleBO) {

        checkRole(roleBO);

        Role role = RoleConverter.INSTANCE.bo2do(roleBO);
        //role.setEnableStatus(SysConstants.EnableStatus.enable.getCode());
        role.setCreatedDate(new Date());
        roleMapper.insertUseGeneratedKeys(role);

        //角色菜单
        List<Long> menuIdList = getFixMenuIds(roleBO.getMenuIdList());

        List<RoleMenu> roleMenuList = menuIdList.stream().map(menuNo -> new RoleMenu(true)
                        .setMenuNo(menuNo)
                        .setRoleId(role.getId()))
                .collect(Collectors.toList());
        roleMenuMapper.insertList(roleMenuList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editRole(RoleBO roleBO) {

        checkRole(roleBO);

        Role role = RoleConverter.INSTANCE.bo2do(roleBO);

        roleMapper.updateByPrimaryKeySelective(role);

        //角色菜单
        roleMenuMapper.logicDelByRoleId(role.getId());

        List<Long> menuIdList = getFixMenuIds(roleBO.getMenuIdList());

        List<RoleMenu> roleMenuList = menuIdList.stream().map(menuNo -> new RoleMenu(true)
                .setMenuNo(menuNo)
                .setRoleId(role.getId())).collect(Collectors.toList());

        roleMenuMapper.insertList(roleMenuList);
    }

    /**
     * 将缺失的夫级id补全
     *
     * @param menuIdList 页面传递的id集合
     */
    private List<Long> getFixMenuIds(List<Long> menuIdList) {
        //把已有的子菜单id转为Set
        HashSet<Long> parentids = new HashSet<>(menuIdList);

        //Long转String,根据01判断一级父id,每多一级长度加2,截取下一级父id,添加set
        menuIdList.stream().map(String::valueOf).forEach(menuId -> {
            int length = menuId.length();
            int rootIndex = menuId.indexOf("01") + 2;
            if (rootIndex != -1) {
                for (int i = rootIndex; i < length; i += 2) {
                    String parentIdStr = menuId.substring(0, i);
                    parentids.add(Long.parseLong(parentIdStr));
                }
            }
        });
        return new ArrayList<>(parentids);
    }

    /**
     * 检查角色关键信息
     *
     * @param roleBO
     */
    private void checkRole(RoleBO roleBO) {
        if (roleBO.getRoleId() == null) {
            int cnt = roleMapper.selectCntByName(roleBO.getRoleName());
            if (cnt > 0) {
                throw new AppServerException(SystemMessage.FAILURE.getCode(), "相同角色已存在");
            }
        } else {
            Role dbRole = roleMapper.selectByName(roleBO.getRoleName());
            if (dbRole != null && !dbRole.getId().equals(roleBO.getRoleId())) {
                throw new AppServerException(SystemMessage.FAILURE.getCode(), "相同角色已存在");
            }
        }

        if (CollectionUtils.isEmpty(roleBO.getMenuIdList())) {
            throw new AppServerException(SystemMessage.FAILURE.getCode(), "菜单配置不能为空");
        }
    }

    public RoleDTO roleInfo(Long id) {
        RoleDTO roleDTO = null;
        Role role = roleMapper.selectById(id);
        if (role != null) {
            roleDTO = new RoleDTO();
            BeanUtils.copyProperties(role, roleDTO);
        }
        return roleDTO;
    }

    public void deleteRole(Long id) {
        if (MngContextHolder.ADMIN_ROLE_ID.equals(id)) {
            throw new AppServerException(SystemMessage.FAILURE.getCode(), "管理员角色不能被删除");
        }

        List<UserRoleRelation> relations = userRoleRelationMapper.selectUserRoleRelationList(new UserRoleRelation().setRoleId(id));
        if (CollectionUtils.isNotEmpty(relations)) {
            throw new AppServerException(SystemMessage.FAILURE.getCode(), "请取消相关账号与该角色的关联后再删除");
        }
        roleMapper.logicDeleteById(id);
    }

}
