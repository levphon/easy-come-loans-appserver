package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.UserProfile;
import cn.com.payu.app.modules.model.MngCUserDTO;
import cn.com.payu.app.modules.model.export.MngCUserExport;
import cn.com.payu.app.modules.model.params.CustUserSearch;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserProfileMapper extends CommonBaseMapper<UserProfile> {

    UserProfile selectByUserId(@Param("userId") Long userId);

    int logicDeleteById(@Param("id") Long id);

    int logicDeleteByUserId(@Param("userId") Long userId);

    List<MngCUserDTO> search(CustUserSearch search);

    List<MngCUserExport> export(CustUserSearch search);

}