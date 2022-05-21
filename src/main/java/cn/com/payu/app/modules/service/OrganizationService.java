package cn.com.payu.app.modules.service;


import cn.com.payu.app.common.constant.Constants;
import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.converter.DepartmentConverter;
import cn.com.payu.app.modules.entity.Department;
import cn.com.payu.app.modules.entity.Organization;
import cn.com.payu.app.modules.entity.Tenant;
import cn.com.payu.app.modules.mapper.DepartmentMapper;
import cn.com.payu.app.modules.mapper.MngUserMapper;
import cn.com.payu.app.modules.mapper.OrganizationMapper;
import cn.com.payu.app.modules.mapper.TenantMapper;
import cn.com.payu.app.modules.model.params.OrgTreeSearch;
import cn.com.payu.app.modules.model.params.OrganizationBO;
import cn.com.payu.app.modules.model.tree.OrgModel;
import cn.com.payu.app.modules.model.tree.OrgTreeModel;
import cn.com.payu.app.modules.model.view.DepartmentDTO;
import cn.com.payu.app.modules.service.permissionStrategy.PermissionStrategy;
import cn.com.payu.app.modules.utils.MngContextHolder;
import cn.hutool.json.JSONUtil;
import com.glsx.plat.common.model.TreeModel;
import com.glsx.plat.common.utils.StringUtils;
import com.glsx.plat.common.utils.TreeModelUtil;
import com.glsx.plat.exception.SystemMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.com.payu.app.common.constant.UserConstants.RolePermitCastType.getBeanNameByCode;


/**
 * @author: taoyr
 **/
@Slf4j
@Service
public class OrganizationService {

    private final Map<String, PermissionStrategy> permissionStrategyMap;
    @Resource
    private OrganizationMapper organizationMapper;
    @Resource
    private TenantMapper tenantMapper;
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private MngUserMapper mngUserMapper;

    public OrganizationService(Map<String, PermissionStrategy> permissionStrategyMap) {
        this.permissionStrategyMap = permissionStrategyMap;
    }

    /**
     * 插入根节点路径
     *
     * @param orgBO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Organization addRootOrganization(OrganizationBO orgBO) {
        Tenant duplicateNameTenant = tenantMapper.selectOne(new Tenant().setTenantName(orgBO.getDepartmentName()));
        if (duplicateNameTenant != null) {
            throw new AppServerException(SystemMessage.FAILURE.getCode(), "已有同名的根节点");
        }

        Tenant tenant = new Tenant(true);
        tenant.setTenantName(orgBO.getDepartmentName());
        tenantMapper.insertUseGeneratedKeys(tenant);

        Long tenantId = tenant.getId();

        Department department = new Department(true);
        department.setEnableStatus(orgBO.getEnableStatus());
        department.setTenantId(tenantId);
        department.setIsRoot(Constants.IS_ROOT_DEPARTMENT);
        department.setOrderNum(orgBO.getOrderNum());
        department.setDepartmentName(orgBO.getDepartmentName());
        departmentMapper.insertUseGeneratedKeys(department);

        Long departmentId = department.getId();

        Organization organization = new Organization(true);
        organization.setSuperiorId(departmentId);
        organization.setSubId(departmentId);
        organization.setTenantId(tenantId);
        organizationMapper.insertRootPath(organization);
        log.info("新增根组织关系{}", organization);
        return organization;
    }

    /**
     * 增加组织树节点
     *
     * @param organizationBO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Organization addNodeToOrganization(OrganizationBO organizationBO) {
        Long superiorId = organizationBO.getSuperiorId();

        //选中的上级组织
        Department parentDept = departmentMapper.selectById(superiorId);
        if (parentDept == null) {
            throw new AppServerException(SystemMessage.FAILURE.getCode(), "上级组织已删除，请刷新页面");
        }

        Long tenantId = parentDept.getTenantId();

        Department department = new Department(true);
        department.setTenantId(tenantId);
        department.setEnableStatus(organizationBO.getEnableStatus());
        department.setOrderNum(organizationBO.getOrderNum());
        department.setIsRoot(Constants.IS_NOT_ROOT_DEPARTMENT);
        department.setDepartmentName(organizationBO.getDepartmentName());
        departmentMapper.insertUseGeneratedKeys(department);

        Long departmentId = department.getId();

        Organization organization = new Organization(true);
        organization.setSuperiorId(superiorId);
        organization.setSubId(departmentId);
        organization.setTenantId(tenantId);
        int insertCnt = organizationMapper.insertOrgPath(organization);
        log.info("新增组织{}关系{}条", departmentId, insertCnt);
        return organization;
    }

    /**
     * 获取组织机构树
     *
     * @param search
     * @return
     */
    public List<? extends TreeModel> fullOrgTree(OrgTreeSearch search) {
        List<OrgModel> modelList = organizationMapper.selectOrgList(search);
        List<OrgTreeModel> orgTreeModelList = modelList.stream().map(OrgTreeModel::new).sorted(Comparator.comparing(OrgTreeModel::getOrder)).collect(Collectors.toList());
        List<? extends TreeModel> orgTree = TreeModelUtil.fastConvertByRootMark(orgTreeModelList, 1);
        return orgTree;
    }

    @Transactional(rollbackFor = Exception.class)
    public void editOrganization(OrganizationBO orgBO) {
        Long organizationId = orgBO.getId();

        Department department = departmentMapper.selectById(organizationId);
        if (department == null) {
            throw new AppServerException(SystemMessage.FAILURE.getCode(), "组织部门不存在");
        }
        department.setOrderNum(orgBO.getOrderNum());
        department.setDepartmentName(orgBO.getDepartmentName());
        department.setEnableStatus(orgBO.getEnableStatus());
        department.setUpdatedDate(new Date());
        department.setUpdatedBy(MngContextHolder.getUserId());
        departmentMapper.updateByPrimaryKeySelective(department);

        //如果是根组织部门，则同时修改租户
        if (department.getIsRoot() == Constants.IS_ROOT_DEPARTMENT) {
            Tenant tenant = tenantMapper.selectById(department.getTenantId());
            if (tenant == null) {
                throw new AppServerException(SystemMessage.FAILURE.getCode(), "组织租户不存在");
            }
            tenant.setTenantName(orgBO.getDepartmentName());
            tenantMapper.updateByPrimaryKeySelective(tenant);
        }
    }

    /**
     * 删除组织
     *
     * @param organizationId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrganization(Long organizationId) {
        Department department = departmentMapper.selectById(organizationId);
        if (department == null) {
            throw new AppServerException(SystemMessage.FAILURE.getCode(), "该组织不存在，请重新刷新列表");
        }

        List<Organization> organizations = organizationMapper.selectAllSubBySuperiorId(organizationId);

        List<Long> organizationIdList = organizations.stream().map(Organization::getSubId).collect(Collectors.toList());

        //删除包括自己和下级所有部门
        departmentMapper.logicDeleteByIdList(organizationIdList);

        //删除自己上下级及所有下级层级之间 所有路径
        organizationMapper.deleteOrgAllPath(organizationId);

        //删除租户
        if (Constants.IS_ROOT_DEPARTMENT == department.getIsRoot()) {
            Long tenantId = department.getTenantId();
            tenantMapper.logicDeleteById(tenantId);
        }
    }

    public DepartmentDTO organizationInfo(Long organizationId) {
        DepartmentDTO departmentDTO = null;
        Department department = departmentMapper.selectById(organizationId);
        if (department != null) {
            departmentDTO = DepartmentConverter.INSTANCE.do2dto(department);
            if (department.getIsRoot() == Constants.IS_NOT_ROOT_DEPARTMENT) {
                Organization superiorOrg = organizationMapper.selectSuperiorOrgByDepth(department.getId(), 1);
                if (superiorOrg != null) {
                    Department superiorDept = departmentMapper.selectById(superiorOrg.getSuperiorId());
                    departmentDTO.setSuperiorId(superiorDept.getId());
                    departmentDTO.setSuperiorName(superiorDept.getDepartmentName());
                }
            }
        }
        return departmentDTO;
    }

    /**
     * 用户角色权限策略
     *
     * @return
     */
    public PermissionStrategy getPermissionStrategy() {
        Integer rolePermissionType = MngContextHolder.getRolePermissionType();

        String beanName = getBeanNameByCode(rolePermissionType);

        if (StringUtils.isBlank(beanName)) {
            throw new AppServerException(SystemMessage.FAILURE.getCode(), "角色权限类型未知");
        }

        PermissionStrategy permissionStrategy = permissionStrategyMap.get(beanName);

        return permissionStrategy;
    }

    /**
     * @param rootId
     * @return
     */
    public List<DepartmentDTO> simpleList(Long rootId) {
        return this.getPermissionStrategy().orgSimpleList(rootId);
    }

    public List orgTree(OrgTreeSearch search) {
        log.info("OrgTree OrgTreeSearch:{}", JSONUtil.toJsonStr(search));

        List<? extends TreeModel> treeModels = this.getPermissionStrategy().orgTree(search);

        treeModels.sort(Comparator.comparing(TreeModel::getOrder));

        return treeModels;
    }

}
