package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.Order;
import cn.com.payu.app.modules.model.MngOrderDTO;
import cn.com.payu.app.modules.model.MyOrderGoodsDTO;
import cn.com.payu.app.modules.model.OrderSummaryModel;
import cn.com.payu.app.modules.model.StatisticsModel;
import cn.com.payu.app.modules.model.export.MngOrderExport;
import cn.com.payu.app.modules.model.params.OrderSearch;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper extends CommonBaseMapper<Order> {

    Order selectById(@Param("id") Long id);

    Order selectByOrderNo(@Param("orderNo") String orderNo);

    List<MyOrderGoodsDTO> selectByOrderType(@Param("orderType") Integer orderType, @Param("userId") Long userId);

    void updateSuccStatusById(@Param("id") Long id, @Param("appId") String appId);

    void updateFailStatusById(@Param("id") Long id);

    void closeOrder(@Param("id") Long id);

    List<MngOrderDTO> search(OrderSearch search);

    List<MngOrderExport> export(OrderSearch search);

    List<StatisticsModel> selectByChannel(@Param("channel") String channel);

    List<OrderSummaryModel> selectSummary(@Param("statDate") String statDate);

}