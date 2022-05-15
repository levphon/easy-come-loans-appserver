package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.EventTracking;
import cn.com.payu.app.modules.model.ChannelUniqueVisitor;
import cn.com.payu.app.modules.model.ClickSummaryDTO;
import cn.com.payu.app.modules.model.params.ChannelSearch;
import cn.com.payu.app.modules.model.params.SummarySearch;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventTrackingMapper extends CommonBaseMapper<EventTracking> {

    List<ChannelUniqueVisitor> selectByChannel(ChannelSearch search);

    ClickSummaryDTO selectStatCnt(SummarySearch search);

}