package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.Vip;
import cn.com.payu.app.modules.model.VipDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VipConverter {

    VipConverter INSTANCE = Mappers.getMapper(VipConverter.class);

    VipDTO do2dto(Vip vip);

}
