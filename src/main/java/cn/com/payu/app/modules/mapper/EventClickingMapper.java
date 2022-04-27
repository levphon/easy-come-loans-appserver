package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.EventClicking;
import cn.com.payu.app.modules.model.StatisticsModel;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventClickingMapper extends CommonBaseMapper<EventClicking> {

    List<StatisticsModel> selectByChannel(@Param("channel") String channel);

}