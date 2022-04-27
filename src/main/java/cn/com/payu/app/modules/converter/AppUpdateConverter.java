package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.AppUpdate;
import cn.com.payu.app.modules.model.AppUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppUpdateConverter {

    AppUpdateConverter INSTANCE = Mappers.getMapper(AppUpdateConverter.class);

    AppUpdateDTO do2dto(AppUpdate update);

}
