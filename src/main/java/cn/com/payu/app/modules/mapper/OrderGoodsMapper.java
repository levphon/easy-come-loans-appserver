package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.OrderGoods;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderGoodsMapper extends CommonBaseMapper<OrderGoods> {

    OrderGoods selectByOrderId(@Param("orderId") Long orderId);

}