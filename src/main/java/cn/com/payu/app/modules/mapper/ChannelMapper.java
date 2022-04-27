package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.Channel;
import cn.com.payu.app.modules.model.MngChannelDTO;
import cn.com.payu.app.modules.model.SimpleChannelDTO;
import cn.com.payu.app.modules.model.export.MngChannelExport;
import cn.com.payu.app.modules.model.params.ChannelSearch;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ChannelMapper extends CommonBaseMapper<Channel> {

    Channel selectById(@Param("id") Long id);

    List<Channel> selectByIds(@Param("ids") Collection<Long> ids);

    List<MngChannelDTO> search(ChannelSearch search);

    List<MngChannelExport> export(ChannelSearch search);

    List<SimpleChannelDTO> selectSimpleList();

    int updateEnableStatusById(@Param("id") Long id, @Param("enableStatus") Integer enableStatus);

    Channel selectByChannelCode(@Param("channel") String channel);

    int countByChannelCode(@Param("channel") String channel);

}