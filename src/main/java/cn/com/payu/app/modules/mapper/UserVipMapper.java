package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.UserVip;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserVipMapper extends CommonBaseMapper<UserVip> {

    UserVip selectByUserId(@Param("userId") Long userId);

    void updateStatusByUserId(@Param("userId") Long userId, @Param("status") Integer status);

    int saveOrUpdate(UserVip userVip);

}