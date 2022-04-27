package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.OrderApplication;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderApplicationMapper extends CommonBaseMapper<OrderApplication> {

    OrderApplication selectByUserId(@Param("userId") Long userId);

    int updateStatusById(@Param("id") Long id, @Param("status") Integer status);

}