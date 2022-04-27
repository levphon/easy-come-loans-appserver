package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.OrderChargeItem;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderChargeItemMapper extends CommonBaseMapper<OrderChargeItem> {

    OrderChargeItem selectByOrderId(@Param("orderId") Long orderId);

    int updateStatusByOrderId(@Param("orderId") Long orderId, @Param("status") String status);

}