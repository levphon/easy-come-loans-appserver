package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.MngUserPath;
import cn.com.payu.app.modules.model.DepartmentUserCount;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface MngUserPathMapper extends CommonBaseMapper<MngUserPath> {

    /**
     * 插入根节点路径
     *
     * @param userPath
     * @return
     */
    int insertRootPath(MngUserPath userPath);

    /**
     * 插入闭包路径
     *
     * @param userPath
     * @return
     */
    int insertUserPath(MngUserPath userPath);

    /**
     * 删除到上级闭包路径
     *
     * @param nodeId
     * @return
     */
    int deleteUserSuperiorPath(Long nodeId);

    /**
     * 删除到下级闭包路径
     *
     * @param nodeId
     * @return
     */
    int deleteUserSubPath(Long nodeId);

    /**
     * 删除全部闭包路径
     *
     * @param nodeId
     * @return
     */
    int deleteUserAllPath(Long nodeId);

    /**
     * 找到根路径
     *
     * @param tenantId
     * @return
     */
    List<MngUserPath> selectRootSubAllPaths(@Param("tenantId") Long tenantId);

    /**
     * 找到根路径
     *
     * @param subId
     * @return
     */
    MngUserPath selectRootPathBySubId(@Param("subId") Long subId);

    /**
     * 得到用户所有上级用户（含自己）
     *
     * @param subId
     * @return
     */
    List<MngUserPath> selectAllSuperiorBySubId(@Param("subId") Long subId);

    /**
     * 得到用户所有下级用户（含自己）
     *
     * @param superiorId
     * @return
     */
    List<MngUserPath> selectSubordinateBySuperiorId(@Param("superiorId") Long superiorId);

    /**
     * 统计用户下级各部门用户数
     *
     * @param superiorId
     * @param userIds
     * @param orgIds
     * @param subOrgIds
     * @return
     */
    List<DepartmentUserCount> selectSubordinateDepartmentList(@Param("superiorId") Long superiorId,
                                                              @Param("userIds") Collection<Long> userIds,
                                                              @Param("orgIds") Collection<Long> orgIds,
                                                              @Param("subOrgIds") Collection<Long> subOrgIds);

}