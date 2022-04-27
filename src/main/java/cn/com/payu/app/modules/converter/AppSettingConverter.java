package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.AppSetting;
import cn.com.payu.app.modules.model.AppSettingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppSettingConverter {

    AppSettingConverter INSTANCE = Mappers.getMapper(AppSettingConverter.class);

    AppSettingDTO do2dto(AppSetting setting);

}
