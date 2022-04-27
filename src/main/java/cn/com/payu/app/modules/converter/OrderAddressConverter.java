package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.OrderAddress;
import cn.com.payu.app.modules.model.OrderAddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderAddressConverter {

    OrderAddressConverter INSTANCE = Mappers.getMapper(OrderAddressConverter.class);

    OrderAddressDTO do2dto(OrderAddress item);

}
