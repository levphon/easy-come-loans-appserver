package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.UserBankCard;
import cn.com.payu.app.modules.model.params.BindBankCardBO;
import cn.com.payu.app.modules.model.BindBankCardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserBankCardConverter {

    UserBankCardConverter INSTANCE = Mappers.getMapper(UserBankCardConverter.class);

    UserBankCard bo2do(BindBankCardBO bankCardBO);

    BindBankCardDTO do2dto(UserBankCard bankCard);

}
