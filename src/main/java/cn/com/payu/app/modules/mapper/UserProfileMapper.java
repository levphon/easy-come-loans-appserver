package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.UserProfile;
import cn.com.payu.app.modules.model.CustUserRegisterListDTO;
import cn.com.payu.app.modules.model.export.CustUserRegisterExport;
import cn.com.payu.app.modules.model.params.CustUserSearch;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserProfileMapper extends CommonBaseMapper<UserProfile> {

    UserProfile selectByUserId(@Param("userId") Long userId);

    int logicDeleteById(@Param("id") Long id);

    int logicDeleteByUserId(@Param("userId") Long userId);

    List<CustUserRegisterListDTO> registerSearch(CustUserSearch search);

    List<CustUserRegisterExport> registerExport(CustUserSearch search);

}