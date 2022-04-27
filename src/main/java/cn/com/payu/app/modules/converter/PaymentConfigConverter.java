package cn.com.payu.app.modules.converter;

import cn.com.payu.app.config.AlipayConfig;
import cn.com.payu.app.config.AlipayConfigBak1;
import cn.com.payu.app.modules.entity.PaymentConfig;
import cn.com.payu.app.modules.model.AlipayConfigModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentConfigConverter {

    PaymentConfigConverter INSTANCE = Mappers.getMapper(PaymentConfigConverter.class);

    AlipayConfigModel do2model(PaymentConfig item);

    AlipayConfigModel bean2model(AlipayConfig item);

    AlipayConfigModel bean2model(AlipayConfigBak1 item);

}
