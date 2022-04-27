package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.AppSetting;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppSettingMapper extends CommonBaseMapper<AppSetting> {

    List<AppSetting> list(@Param("type") String type);

}