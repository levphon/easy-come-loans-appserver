package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.CustomerServiceConfig;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerServiceConfigMapper extends CommonBaseMapper<CustomerServiceConfig> {

    CustomerServiceConfig selectById(@Param("id") Long id);

}