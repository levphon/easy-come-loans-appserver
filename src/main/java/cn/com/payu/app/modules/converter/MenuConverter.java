package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.Menu;
import cn.com.payu.app.modules.model.params.MenuBO;
import cn.com.payu.app.modules.model.MenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuConverter {

    MenuConverter INSTANCE = Mappers.getMapper(MenuConverter.class);

    MenuDTO do2dto(Menu menu);

    Menu bo2do(MenuBO menuBO);

}
