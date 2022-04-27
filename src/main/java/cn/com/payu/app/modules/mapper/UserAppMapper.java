package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.UserApp;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAppMapper extends CommonBaseMapper<UserApp> {

    UserApp selectByUserId(@Param("userId") Long userId);

    int updateClientIdByUserId(@Param("userId") Long userId, @Param("clientId") String clientId);

    int saveOrUpdate(UserApp userApp);

}