package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.UserSetting;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserSettingMapper extends CommonBaseMapper<UserSetting> {

    UserSetting selectById(@Param("id") Long id);

    UserSetting selectByUserIdAndSettingId(@Param("userId") Long userId, @Param("settingId") Long settingId);

    int saveOrUpdate(UserSetting setting);

}