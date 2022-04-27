package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.UserRoleRelation;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserRoleRelationMapper extends CommonBaseMapper<UserRoleRelation> {

    /**
     * 获取用户角色列表
     *
     * @param relation
     * @return
     */
    List<UserRoleRelation> selectUserRoleRelationList(UserRoleRelation relation);

    List<UserRoleRelation> selectByUserId(@Param("userId") Long userId);

    Set<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    List<UserRoleRelation> selectByRoleId(@Param("roleId") Long roleId);

}