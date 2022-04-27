package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.CustomerServiceConfig;
import cn.com.payu.app.modules.model.CustomerServiceConfigDTO;
import cn.com.payu.app.modules.model.params.MngCustomerServiceConfigBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerServiceConfigConverter {

    CustomerServiceConfigConverter INSTANCE = Mappers.getMapper(CustomerServiceConfigConverter.class);

    CustomerServiceConfigDTO do2dto(CustomerServiceConfig item);

    CustomerServiceConfig bo2do(MngCustomerServiceConfigBO item);

}
