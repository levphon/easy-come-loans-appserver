package cn.com.payu.app.modules.service.permissionStrategy;

import cn.com.payu.app.modules.entity.Department;
import cn.com.payu.app.modules.entity.MngUser;
import cn.com.payu.app.modules.entity.Organization;
import cn.com.payu.app.modules.model.DepartmentUserCount;
import cn.com.payu.app.modules.model.params.DepartmentSearch;
import cn.com.payu.app.modules.model.params.MngUserSearch;
import cn.com.payu.app.modules.model.params.OrgTreeSearch;
import cn.com.payu.app.modules.model.params.OrganizationSearch;
import cn.com.payu.app.modules.model.tree.OrgModel;
import cn.com.payu.app.modules.model.tree.OrgTreeModel;
import cn.com.payu.app.modules.model.view.DepartmentDTO;
import cn.com.payu.app.modules.service.DepartmentService;
import cn.com.payu.app.modules.utils.MngContextHolder;
import com.glsx.plat.common.model.TreeModel;
import com.glsx.plat.common.utils.StringUtils;
import com.glsx.plat.common.utils.TreeModelUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 本人及下属
 *
 * @author taoyr
 */
@Slf4j
@Component
public class SubOrdinateStrategy extends PermissionStrategy {

    @Resource
    private DepartmentService departmentService;

    public SubOrdinateStrategy() {
        super();
    }

    @Override
    public Set<Long> permissionDepartmentIds() {
        List<MngUser> userList = mngUserMapper.selectDepartmentsSubordinate(new MngUserSearch().setUserId(MngContextHolder.getUserId()));
        return userList.stream().map(MngUser::getDepartmentId).collect(Collectors.toSet());
    }

    @Override
    public List<Department> permissionDepartments() {
        List<MngUser> userList = mngUserMapper.selectDepartmentsSubordinate(new MngUserSearch().setUserId(MngContextHolder.getUserId()));

        Set<Long> subUserOrgIdList = userList.stream().map(MngUser::getDepartmentId).collect(Collectors.toSet());

        return departmentMapper.selectByIds(subUserOrgIdList);
    }

    @Override
    public List<MngUser> permissionUsers() {
        Long userId = MngContextHolder.getUserId();
        //获取下级用户所在部门
        Set<Long> subOrgIds = this.permissionDepartmentIds();

        List<MngUser> list = mngUserMapper.selectDepartmentsSubordinate(new MngUserSearch().setUserId(userId).setDepartmentIds(subOrgIds));

        return list;
    }

    @Override
    public List<MngUser> permissionUsersByDepartmentId(Long departmentId) {
        //先获取departmentId部门-本人部门之间的组织节点
        List<Organization> subOrgList = organizationMapper.selectAllSubBySuperiorId(departmentId);

        List<Long> subOrgIdList = subOrgList.stream().map(Organization::getSubId).collect(Collectors.toList());

        //权限内本人及下级全部用户
        List<MngUser> userList = this.permissionUsers();

        //过滤在departmentId部门-本人部门之间的组织节点之间的下级用户
        List<MngUser> list = userList.stream().filter(user -> subOrgIdList.contains(user.getDepartmentId())).collect(Collectors.toList());

        return list;
    }

    /**
     * 4 subordinate
     * * 4.1 root找自己根部门
     * * 4.2 非root
     * * 找到rootId的下级部门alist
     * * 连表查出所有下级用户（及本人）所在所有部门blist
     * * t_organization查询blist是当前alist下级的，存在关系，则为最终部门列表clist
     * * -设置userNum 据前面查出的map
     * * -设置hasChild clist有depth>=1且在blist集合中的下级部门，则为true
     *
     * @param rootId
     * @return
     */
    @Override
    public List<DepartmentDTO> orgSimpleList(Long rootId) {

        Long userId = MngContextHolder.getUserId();

        Long userDeptId = MngContextHolder.getDepartmentId();

        List<Department> departmentParamList = Lists.newArrayList();

        if (rootId == null) {
            Department department = departmentMapper.selectById(userDeptId);

            departmentParamList.add(department);
        } else {
            //所有下级
            List<Organization> childrenOrgList = organizationMapper.selectList(new OrganizationSearch().setSupIds(Lists.newArrayList(userDeptId)).setBiggerDepth(1));

            List<Long> subIdList = childrenOrgList.stream().map(Organization::getSubId).collect(Collectors.toList());

            //加入用户部门
            subIdList.add(userDeptId);

            if (subIdList.contains(rootId)) {
                List<Organization> subOrganizationList = organizationMapper.selectSubOrgList(Lists.newArrayList(rootId), 1);

                List<Long> departmentIdList = subOrganizationList.stream().map(Organization::getSubId).collect(Collectors.toList());

                List<Department> departmentList = Lists.newArrayList();

                if (CollectionUtils.isNotEmpty(departmentIdList)) {
                    departmentList = departmentMapper.selectByIds(departmentIdList);
                }
                departmentParamList.addAll(departmentList);
            }
        }

        return departmentService.getDepartmentAssembled(departmentParamList, false, false);
    }

    /**
     * 只能看到个人和下级所在部门得组织链，并且到下级部门终止
     *
     * @param search
     * @return
     */
    @Override
    public List<? extends TreeModel> orgTree(OrgTreeSearch search) {

        Long tenantId = MngContextHolder.getTenantId();

        Long userId = MngContextHolder.getUserId();

        //如果是搜索search.getUserIds()
        List<DepartmentUserCount> departmentUserCountList = mngUserPathMapper.selectSubordinateDepartmentList(userId,
                search.getUserIds(),
                search.getOrgIds(),
                search.getSubOrgIds());

        //下级部门id
        Set<Long> subDepartmentIdList = departmentUserCountList.stream().map(DepartmentUserCount::getDepartmentId).filter(Objects::nonNull).collect(Collectors.toSet());

        Set<Long> matchedDepartmentIdList = Sets.newHashSet();

        if (StringUtils.isNotEmpty(search.getOrgName())) {
            //模糊搜索得到的部门
            List<Department> namedDepartmentList = departmentMapper.search(new DepartmentSearch().setTenantId(tenantId).setDepartmentName(search.getOrgName()));
            //提取符合搜索条件的部门
            List<Department> finalNamedDepartmentList = namedDepartmentList.stream().filter(department -> subDepartmentIdList.contains(department.getId())).collect(Collectors.toList());
            //符合条件的部门idList
            matchedDepartmentIdList = finalNamedDepartmentList.stream().map(Department::getId).collect(Collectors.toSet());
        } else {
            matchedDepartmentIdList = subDepartmentIdList;
        }
        //如果没找到匹配部门，返回为空（指定授权跨部门情况容易发生）
        if (CollectionUtils.isEmpty(matchedDepartmentIdList)) {
            return Lists.newArrayList();
        }

        //全部上级
        List<Organization> superiorOrgList = organizationMapper.selectList(new OrganizationSearch().setSubIds(matchedDepartmentIdList));
        //所有部门id
        List<Long> allDepartmentIdList = superiorOrgList.stream().map(Organization::getSuperiorId).collect(Collectors.toList());

        allDepartmentIdList.addAll(matchedDepartmentIdList);

        List<Department> allDepartmentList = departmentMapper.selectByIds(allDepartmentIdList);
        //所有组织链
        List<Organization> organizationList = organizationMapper.selectList(new OrganizationSearch().setSubIds(allDepartmentIdList).setSupIds(allDepartmentIdList));

        //过滤一级层次组织
        Map<Long, Organization> organizationMap = organizationList.stream().filter(m -> m.getDepth() == 1).collect(Collectors.toMap(Organization::getSubId, treeModel -> treeModel));

        Map<Long, Long> subordinateUserCountMap = departmentUserCountList.parallelStream().collect(Collectors.groupingBy(DepartmentUserCount::getSuperiorId, Collectors.counting()));

        List<OrgModel> modelList = allDepartmentList.stream().map(dep -> {
            OrgModel orgModel = new OrgModel();
            Organization org = organizationMap.get(dep.getId());
            orgModel.setParentId(org != null ? org.getSuperiorId() : 0L);
            orgModel.setOrgId(dep.getId());
            orgModel.setOrgName(dep.getDepartmentName());
            orgModel.setTenantId(dep.getTenantId());
            Long userNumber = subordinateUserCountMap.get(dep.getId());
            orgModel.setUserNumber(userNumber != null ? Math.toIntExact(userNumber) : 0);
            orgModel.setOrderNum(String.valueOf(dep.getOrderNum()));
            orgModel.setIsRoot(dep.getIsRoot());
            return orgModel;
        }).collect(Collectors.toList());

        List<OrgTreeModel> orgTreeModelList = modelList.stream().map(OrgTreeModel::new).sorted(Comparator.comparing(OrgTreeModel::getOrder)).collect(Collectors.toList());

        //获取用户及下级用户
        List<OrgModel> userPathsOrgModelList = getUserOrgModel(departmentUserCountList);

        Map<Long, List<OrgModel>> groupByOrgIdUserMap = userPathsOrgModelList.stream().collect(Collectors.groupingBy(OrgModel::getParentId));

        int treeSize = orgTreeModelList.size();

        for (int i = 0; i < treeSize; i++) {
            OrgTreeModel otm = orgTreeModelList.get(i);
            Long orgId = Long.valueOf(otm.getId());
            Long userNumber = subordinateUserCountMap.get(orgId);
            otm.setUserNumber(userNumber == null ? 0 : Math.toIntExact(userNumber));
            //2021/8/19 设置下级用户子树
            if (search.getHasUserLeaf()) {
                List<OrgModel> userList = groupByOrgIdUserMap.get(orgId);
                if (CollectionUtils.isNotEmpty(userList)) {
                    List<OrgTreeModel> userTreeList = userList.stream().map(OrgTreeModel::new).collect(Collectors.toList());
                    orgTreeModelList.addAll(userTreeList);
                }
            }
        }

        if (search.getReturnTrees()) {
            List<? extends TreeModel> orgTree = TreeModelUtil.fastConvertByRootMark(orgTreeModelList, 1);
            return orgTree;
        }
        return orgTreeModelList;
    }

    @Override
    public Map<Long, Integer> calculateNumberOfOrgUsers(Collection<Long> orgIds) {
        return null;
    }

    public List<OrgModel> getUserOrgModel(List<DepartmentUserCount> deptUserList) {
        List<OrgModel> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(deptUserList)) {
            return list;
        }

        Set<Long> userSet = deptUserList.stream().map(DepartmentUserCount::getUserId).collect(Collectors.toSet());

        List<MngUser> userList = mngUserMapper.selectByIds(userSet);

        userList.forEach(user -> {
            OrgModel userModel = new OrgModel();
            userModel.setParentId(user.getDepartmentId());
            userModel.setOrgId(user.getId());
            userModel.setOrgName(user.getAccount() + "/" + user.getUsername());
            userModel.setTenantId(user.getTenantId());
            userModel.setUserNumber(0);
            userModel.setType("u");
            userModel.setOrderNum(String.valueOf(user.getId()));
            userModel.setIsRoot(0);
            list.add(userModel);
        });
        return list;
    }

}
