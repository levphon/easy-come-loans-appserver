package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.MngUser;
import cn.com.payu.app.modules.model.params.MngUserSearch;
import cn.com.payu.app.modules.model.tree.OrgModel;
import com.glsx.plat.common.model.DropOptions;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface MngUserMapper extends CommonBaseMapper<MngUser> {

    List<MngUser> selectList(MngUserSearch search);

    MngUser selectById(@Param("id") Long id);

    MngUser selectByAccount(@Param("account") String account);

    List<MngUser> selectByIds(@Param("ids") Collection<Long> ids);

    List<MngUser> selectDepartmentsSubordinate(MngUserSearch search);

    List<MngUser> selectByDepartmentIds(@Param("departmentIds") Collection<Long> departmentIds);

    List<MngUser> selectAllNotDel();

    List<OrgModel> selectUserOrgModels(MngUserSearch search);

    int selectCntByAccount(@Param("account") String username);

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int logicDeleteById(@Param("id") Long id);

    List<DropOptions> selectOptions(@Param("departmentId") Long departmentId, @Param("username") String username);

}