package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.Department;
import cn.com.payu.app.modules.model.UserOrgModel;
import cn.com.payu.app.modules.model.UserSuperiorOrgModel;
import cn.com.payu.app.modules.model.params.DepartmentSearch;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DepartmentMapper extends CommonBaseMapper<Department> {

    List<Department> search(DepartmentSearch search);

    List<Department> selectByIds(@Param("ids") Collection<Long> ids);

    List<Department> selectAllNotDel();

    Department selectById(@Param("id") Long departmentId);

    int logicDeleteByIdList(@Param("ids") Collection<Long> ids);

    /**
     * 根据根组织标识和租户id获取根组织
     *
     * @param tenantId
     * @return
     */
    Department selectRootDepartmentByTenantId(@Param("tenantId") Long tenantId);

    List<Department> selectByTenantId(@Param("tenantId") Long tenantId);

    List<Department> selectByTenantIds(@Param("tenantIds") Collection<Long> tenantIds);

    /**
     * 获取用户所在组织层级
     *
     * @param userIds
     * @return
     */
    List<UserSuperiorOrgModel> getTenantPathsByUserIds(@Param("userIds") Collection<Long> userIds);

    /**
     * 获取租户特定层级组织
     *
     * @param subIds
     * @param depth
     * @return
     */
    List<Department> selectSpecialLevelPaths(@Param("subIds") Collection<Long> subIds, @Param("depths") Collection<Integer> depth);

    List<UserOrgModel> selectUserOrgs(@Param("userIds") Collection<Long> userIds);

}