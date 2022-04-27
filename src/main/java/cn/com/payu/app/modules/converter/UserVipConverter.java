package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.UserVip;
import cn.com.payu.app.modules.model.UserVipDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserVipConverter {

    UserVipConverter INSTANCE = Mappers.getMapper(UserVipConverter.class);

    UserVipDTO do2dto(UserVip userVip);

}
