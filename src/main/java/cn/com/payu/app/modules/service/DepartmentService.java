package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.constant.Constants;
import cn.com.payu.app.modules.entity.*;
import cn.com.payu.app.modules.mapper.*;
import cn.com.payu.app.modules.model.OrgSuperiorModel;
import cn.com.payu.app.modules.model.params.DepartmentSearch;
import cn.com.payu.app.modules.model.params.OrgTreeSearch;
import cn.com.payu.app.modules.model.params.OrganizationSearch;
import cn.com.payu.app.modules.model.view.DepartmentDTO;
import cn.com.payu.app.modules.utils.MngContextHolder;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.com.payu.app.common.constant.UserConstants.RolePermitCastType.*;

/**
 * @author: taoyr
 **/
@Service
public class DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private MngUserMapper mngUserMapper;

    @Resource
    private MngUserPathMapper mngUserPathMapper;

    @Resource
    private TenantMapper tenantMapper;

    /**
     * 获取当前用户权限所拥有的部门
     *
     * @return
     */
    public List<Long> getCurrentUserDepartmentIds() {
        Long roleId = MngContextHolder.getRoleId();
        Long userId = MngContextHolder.getUserId();
        return getRoleDepartment(userId, roleId);
    }

    public List<Long> getRoleDepartment(Long userId, Long roleId) {
        List<Department> departmentList = getUserRoleDepartments(userId, roleId);

        List<Long> departmentIdList = departmentList.stream().map(Department::getId).collect(Collectors.toList());

        return departmentIdList;
    }

    public List<Tenant> getUserRoleTenants(Long userId, Long roleId) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            return Lists.newArrayList();
        }

        MngUser user = mngUserMapper.selectById(userId);
        if (oneself.getCode().equals(role.getRolePermissionType())) {
            Tenant tenant = tenantMapper.selectById(user.getTenantId());
            //自己 无租户权限
            return Lists.newArrayList(tenant);
        } else if (all.getCode().equals(role.getRolePermissionType())) {
            //全部租户
            return tenantMapper.selectAllNotDel();
        } else if (selfDepartment.getCode().equals(role.getRolePermissionType())) {
            //自己部门
            Tenant tenant = tenantMapper.selectById(user.getTenantId());
            return Lists.newArrayList(tenant);
        } else if (subDepartment.getCode().equals(role.getRolePermissionType())) {
            //自己部门+下级部门
            Tenant tenant = tenantMapper.selectById(user.getTenantId());
            return Lists.newArrayList(tenant);
        } else if (subordinate.getCode().equals(role.getRolePermissionType())) {
            //本人及下级 无租户权限
            Tenant tenant = tenantMapper.selectById(user.getTenantId());
            return Lists.newArrayList(tenant);
        }
        return Lists.newArrayList();
    }

    /**
     * 获取角色数据权限内的部门
     *
     * @param userId
     * @param roleId
     * @return
     */
    public List<Department> getUserRoleDepartments(Long userId, Long roleId) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            return Lists.newArrayList();
        }

        if (oneself.getCode().equals(role.getRolePermissionType())) {
            //自己 不能看部门
            return Lists.newArrayList();
        } else if (all.getCode().equals(role.getRolePermissionType())) {
            //全部部门
            return departmentMapper.selectAllNotDel();
        } else if (selfDepartment.getCode().equals(role.getRolePermissionType())) {
            //自己部门
            MngUser user = mngUserMapper.selectById(userId);
            Department department = departmentMapper.selectById(user.getDepartmentId());
            return Lists.newArrayList(department);
        } else if (subDepartment.getCode().equals(role.getRolePermissionType())) {
            //自己部门+下级部门
            MngUser user = mngUserMapper.selectById(userId);

            List<Organization> subOrgList = organizationMapper.selectAllSubBySuperiorId(user.getDepartmentId());

            List<Long> departmentIdList = subOrgList.stream().map(Organization::getSubId).collect(Collectors.toList());

            return departmentMapper.selectByIds(departmentIdList);
        } else if (subordinate.getCode().equals(role.getRolePermissionType())) {
            //本人及下级 不能看部门
//            List<DepartmentUserCount> departmentUserCountList = userPathMapper.selectSubordinateDepartmentList(userId);
//            departmentList = departmentUserCountList.stream().map(DepartmentUserCount::getDepartmentId).collect(Collectors.toList());
            return Lists.newArrayList();
        }
        return Lists.newArrayList();
    }

    /**
     * 获取角色数据权限内的用户
     *
     * @param userId
     * @param roleId
     * @return
     */
    public List<MngUser> getUserRoleUsers(Long userId, Long roleId) {
        List<MngUser> userList = Lists.newArrayList();

        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            return userList;
        }

        //List<Department> departmentList = this.getUserRoleDepartments(userId,roleId);

        if (all.getCode().equals(role.getRolePermissionType())) {
            //全部用户
            return mngUserMapper.selectAllNotDel();
        } else if (subordinate.getCode().equals(role.getRolePermissionType())) {
            //自己+下级用户
            List<MngUserPath> userPathList = mngUserPathMapper.selectSubordinateBySuperiorId(userId);

            List<Long> subUserIdList = userPathList.stream().map(MngUserPath::getSubId).collect(Collectors.toList());

            return mngUserMapper.selectByIds(subUserIdList);
        }
        return userList;
    }

    /**
     * 根部门列表
     *
     * @param search
     * @return
     */
    public PageInfo<DepartmentDTO> rootDepartmentList(OrganizationSearch search) {
        Integer rolePermissionType = MngContextHolder.getRolePermissionType();

        Page page = null;

        DepartmentSearch deptSearch = new DepartmentSearch()
                .setEnableStatus(search.getEnableStatus())
                .setDepartmentName(search.getOrgName())
                .setIsRoot(Constants.IS_ROOT_DEPARTMENT);

        //非admin和非授权全部数据的用户只能看到自己所在的组织
        if (all.getCode().equals(rolePermissionType)) {
            //do nothing
        } else {
            deptSearch.setTenantId(MngContextHolder.getTenantId());
        }

        // 2020/12/4 模糊搜索
        Set<Long> superiorIds = Sets.newHashSet();
        if (StringUtils.isNotEmpty(search.getOrgName())) {
            //得到模糊查询得到的部门的所有上级id
            OrgTreeSearch treeSearch = new OrgTreeSearch()
                    .setTenantId(deptSearch.getTenantId())
                    .setOrgName(deptSearch.getDepartmentName())
                    .setEnableStatus(deptSearch.getEnableStatus())
                    .setTenantIds(deptSearch.getTenantIds());

            List<OrgSuperiorModel> orgSuperiorModelList = organizationMapper.selectSuperiorIdsByOrg(treeSearch);

            superiorIds = getSuperiorIds(orgSuperiorModelList);

            if (CollectionUtils.isEmpty(superiorIds)) {
                superiorIds.add(0L);
            }
            //置空模糊查询条件，根据上级id查根组织
            deptSearch.setOrgIds(superiorIds).setDepartmentName(null).setEnableStatus(null);
        }

        if (search.isForPage()) {
            page = PageHelper.startPage(search.getPageNumber(), search.getPageSize());
        }
        //因为前面已经过滤出精确id，所以这里要去掉搜索条件
        List<Department> rootList = departmentMapper.search(deptSearch);

        List<DepartmentDTO> departmentDTOList = this.getDepartmentAssembled(rootList, search.isHasChild(), search.isHasUserNumber());

        PageInfo<DepartmentDTO> pageInfo = new PageInfo<>(departmentDTOList);
        if (search.isForPage()) {
            pageInfo.setPages(page.getPages());//总页数
            pageInfo.setTotal(page.getTotal());//总条数
        }
        return pageInfo;
    }

    public List<DepartmentDTO> childrenList(OrganizationSearch search) {

        Department department = departmentMapper.selectById(search.getRootId());

        // 2020/12/4 模糊搜索
        if (StringUtils.isNotEmpty(search.getOrgName())) {
            //得到模糊查询得到的部门的所有上级id
            List<OrgSuperiorModel> orgSuperiorModelList = organizationMapper.selectSuperiorIdsByOrg(new OrgTreeSearch()
                    .setTenantId(department.getTenantId())
                    .setOrgName(search.getOrgName())
                    .setEnableStatus(search.getEnableStatus()));

            Set<Long> superiorIds = getSuperiorIds(orgSuperiorModelList);

            //置空模糊查询条件，根据上级id查根组织
            search.setTenantId(department.getTenantId()).setOrgIds(superiorIds).setOrgName(null);
        }

        List<Department> departmentList = organizationMapper.selectChildrenList(search);

        List<DepartmentDTO> departmentDTOList = this.getDepartmentAssembled(departmentList, true, false);

        if (MngContextHolder.isSuperAdmin() || all.getCode().equals(MngContextHolder.getRolePermissionType())) {
            return departmentDTOList;
        }

        //下面代码控制角色的可见权限
        List<Long> departmentIdList = this.getCurrentUserDepartmentIds();

        departmentDTOList = departmentDTOList.stream().filter(d -> departmentIdList.contains(d.getId())).collect(Collectors.toList());

        return departmentDTOList;
    }

    /**
     * 获取上级组织id
     *
     * @param superiorModelList
     * @return
     */
    public Set<Long> getSuperiorIds(List<OrgSuperiorModel> superiorModelList) {
        Set<Long> superiorIds = Sets.newHashSet();
        superiorModelList.forEach(osm -> {
            if (com.glsx.plat.common.utils.StringUtils.isNotEmpty(osm.getSuperiorIds())) {
                String[] ids = osm.getSuperiorIds().split(",");
                for (String id : ids) {
                    superiorIds.add(Long.valueOf(id));
                }
            }
        });
        return superiorIds;
    }

    /**
     * 封装部门数据
     *
     * @param departmentList
     * @param hasChild
     * @param hasUserNumber
     * @return
     */
    public List<DepartmentDTO> getDepartmentAssembled(List<Department> departmentList, final boolean hasChild, final boolean hasUserNumber) {

        List<Long> departmentIds = departmentList.stream().map(Department::getId).collect(Collectors.toList());

        List<DepartmentDTO> departmentDTOList = departmentList.stream().map(dep -> {
                    DepartmentDTO departmentDTO = new DepartmentDTO();
                    BeanUtils.copyProperties(dep, departmentDTO);
                    return departmentDTO;
                }
        ).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(departmentIds)) {
            if (hasChild) {
                List<Organization> organizationList = organizationMapper.selectSubOrgList(departmentIds, 1);

                Map<Long, List<Long>> subOrganizationMap = organizationList.stream().collect(Collectors.toMap(Organization::getSuperiorId, org -> Lists.newArrayList(org.getSubId()),
                        (List<Long> newValueList, List<Long> oldValueList) -> {
                            oldValueList.addAll(newValueList);
                            return oldValueList;
                        }));

                departmentDTOList.forEach(dep -> dep.setHasChildren(subOrganizationMap.get(dep.getId()) != null));
            }
        }
        return departmentDTOList;
    }

}
