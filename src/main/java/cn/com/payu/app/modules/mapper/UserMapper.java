package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.User;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends CommonBaseMapper<User> {

    User selectById(@Param("id") Long id);

    User selectByAccount(@Param("account") String account, @Param("fromApp") Integer fromApp);

    User selectByPhoneMd5(@Param("phoneMd5") String phoneMd5);

    int saveOrUpdate(User user);

    int logicDeleteById(@Param("id") Long id);

}