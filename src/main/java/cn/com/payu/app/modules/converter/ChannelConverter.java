package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.Channel;
import cn.com.payu.app.modules.model.MngChannelBO;
import cn.com.payu.app.modules.model.MngChannelDTO;
import cn.com.payu.app.modules.model.MngChannelInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChannelConverter {

    ChannelConverter INSTANCE = Mappers.getMapper(ChannelConverter.class);

    MngChannelDTO do2dto(Channel item);

    MngChannelInfoDTO do2infoDto(Channel item);

    Channel bo2do(MngChannelBO item);

}
