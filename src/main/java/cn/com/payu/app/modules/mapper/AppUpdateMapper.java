package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.AppUpdate;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AppUpdateMapper extends CommonBaseMapper<AppUpdate> {

    AppUpdate selectByName(@Param("name") String name);

}