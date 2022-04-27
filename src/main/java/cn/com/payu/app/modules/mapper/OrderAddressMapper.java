package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.OrderAddress;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderAddressMapper extends CommonBaseMapper<OrderAddress> {

    OrderAddress selectByOrderId(@Param("orderId") Long orderId);

}