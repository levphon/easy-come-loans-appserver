package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.UserLocalAccount;
import cn.com.payu.app.modules.model.AppUser;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLocalAccountMapper extends CommonBaseMapper<UserLocalAccount> {

    UserLocalAccount selectByAuthId(@Param("authId") Long authId);

    UserLocalAccount selectByUserId(@Param("userId") Long userId);

    AppUser selectAppUserByUserId(@Param("userId") Long userId);

    UserLocalAccount selectByUnionId(@Param("unionId") String unionId);

    int updateIMTokenById(@Param("id") Long id, @Param("token") String token);

    int logicDeleteById(@Param("id") Long id);

    int logicDeleteByUserId(@Param("userId") Long userId);

}