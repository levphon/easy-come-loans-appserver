package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.UserLocalAuth;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLocalAuthMapper extends CommonBaseMapper<UserLocalAuth> {

    UserLocalAuth selectById(@Param("id") Long id);

    UserLocalAuth selectByUserId(@Param("userId") Long userId);

    int updateMobileByUserId(@Param("userId") Long userId, @Param("mobile") String mobile);

    int updatePasswordByUserId(@Param("userId") Long userId, @Param("password") String password);

    int logicDeleteById(@Param("id") Long id);

}