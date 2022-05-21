package cn.com.payu.app.modules.service.permissionStrategy;

import cn.com.payu.app.modules.entity.Department;
import cn.com.payu.app.modules.entity.MngUser;
import cn.com.payu.app.modules.entity.Organization;
import cn.com.payu.app.modules.mapper.DepartmentMapper;
import cn.com.payu.app.modules.mapper.MngUserMapper;
import cn.com.payu.app.modules.mapper.MngUserPathMapper;
import cn.com.payu.app.modules.mapper.OrganizationMapper;
import cn.com.payu.app.modules.model.OrgSuperiorModel;
import cn.com.payu.app.modules.model.params.OrgTreeSearch;
import cn.com.payu.app.modules.model.view.DepartmentDTO;
import com.glsx.plat.common.model.TreeModel;
import com.glsx.plat.context.utils.SpringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author taoyr
 */
public abstract class PermissionStrategy {

    protected MngUserMapper mngUserMapper;

    protected MngUserPathMapper mngUserPathMapper;

    protected DepartmentMapper departmentMapper;

    protected OrganizationMapper organizationMapper;

    public PermissionStrategy() {
        this.mngUserMapper = SpringUtils.getBean("mngUserMapper");
        this.mngUserPathMapper = SpringUtils.getBean("mngUserPathMapper");
        this.departmentMapper = SpringUtils.getBean("departmentMapper");
        this.organizationMapper = SpringUtils.getBean("organizationMapper");
    }

    /**
     * 根据角色数据权限范围获取对应的部门（组织）id
     *
     * @return
     */
    public abstract Collection<Long> permissionDepartmentIds();

    /**
     * 根据角色数据权限范围获取对应的部门（组织）
     *
     * @return
     */
    public abstract List<Department> permissionDepartments();

    /**
     * 根据角色数据权限范围获取对应的人
     *
     * @return
     */
    public abstract List<MngUser> permissionUsers();

    /**
     * 根据角色数据权限范围获取对应的人 根据部门(组织)id
     *
     * @return
     */
    public abstract List<MngUser> permissionUsersByDepartmentId(Long departmentId);

    /**
     * 获取组织机构列表，需求特殊处理（比如本人角色数据权限为本人，但是要求能看的他自己的（包含自己建和非自己建）上级组织结构数据）
     *
     * @param rootId
     * @return
     */
    public abstract List<DepartmentDTO> orgSimpleList(Long rootId);

    /**
     * 获取组织树
     *
     * @param search
     * @return
     */
    public abstract List<? extends TreeModel> orgTree(OrgTreeSearch search);

    /**
     * 计算组织用户数量
     *
     * @param orgIds
     * @return
     */
    public abstract Map<Long, Integer> calculateNumberOfOrgUsers(Collection<Long> orgIds);

    /**
     * 获取组织到根组织的组织链
     *
     * @param search
     * @return
     */
    public Collection<Long> getSuperiorIds(OrgTreeSearch search) {
        List<OrgSuperiorModel> superiorModelList = organizationMapper.selectSuperiorIdsByOrg(search);
        if (CollectionUtils.isEmpty(superiorModelList)) {
            return Lists.newArrayList();
        }
        Collection<Long> superiorIds = this.getSuperiorIds(superiorModelList);
        return superiorIds;
    }

    /**
     * 获取组织到根组织的组织链
     *
     * @param orgIds
     * @return
     */
    public Collection<Long> getSuperiorIds(Collection<Long> orgIds) {
        List<OrgSuperiorModel> superiorModelList = organizationMapper.selectSuperiorIdsByOrgIds(orgIds);
        if (CollectionUtils.isEmpty(superiorModelList)) {
            return Lists.newArrayList();
        }
        Collection<Long> superiorIds = this.getSuperiorIds(superiorModelList);
        return superiorIds;
    }

    /**
     * 获取组织到最后一级组织的组织链
     *
     * @param orgIds
     * @return
     */
    public Collection<Long> getSubIds(Collection<Long> orgIds) {
        Map<Long, Set<Long>> subDepartmentIdListMap = getSubIdsMap(orgIds);

        Set<Long> subOrgIds = Sets.newHashSet();
        for (Map.Entry<Long, Set<Long>> entry : subDepartmentIdListMap.entrySet()) {
            subOrgIds.addAll(entry.getValue());
        }
        return subOrgIds;
    }

    /**
     * 获取上级组织id
     *
     * @param superiorModelList
     * @return
     */
    public Collection<Long> getSuperiorIds(List<OrgSuperiorModel> superiorModelList) {
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
     * 获取用户当前组织到根组织的组织链
     *
     * @param userIds
     * @return
     */
    public Collection<Long> getUserOrgSuperiorIds(Collection<Long> userIds) {
        List<MngUser> userList = mngUserMapper.selectByIds(userIds);

        Set<Long> departmentIdSet = userList.stream().map(MngUser::getDepartmentId).collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(departmentIdSet)) {
            return Lists.newArrayList();
        }

        List<OrgSuperiorModel> superiorModelList = organizationMapper.selectSuperiorIdsByOrg(new OrgTreeSearch().setOrgIds(departmentIdSet));

        Collection<Long> superiorIdSet = this.getSuperiorIds(superiorModelList);

        return superiorIdSet;
    }

    /**
     * 获取多组织到最后一级组织的组织链
     *
     * @param orgIds
     * @return
     */
    public Map<Long, Set<Long>> getSubIdsMap(Collection<Long> orgIds) {
        List<Organization> allSubList = organizationMapper.selectSubOrgList(orgIds, null);

        Map<Long, Set<Long>> subDepartmentIdListMap = allSubList.stream().collect(Collectors.groupingBy(Organization::getSuperiorId))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(Organization::getSubId)
                        .collect(Collectors.toSet())));
        return subDepartmentIdListMap;
    }

}
