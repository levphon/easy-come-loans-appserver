package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.Coupon;
import cn.com.payu.app.modules.entity.CouponRecipients;
import cn.com.payu.app.modules.model.CouponDTO;
import cn.com.payu.app.modules.model.CouponRecipientsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CouponConverter {

    CouponConverter INSTANCE = Mappers.getMapper(CouponConverter.class);

    CouponDTO do2dto(Coupon item);

    CouponRecipientsDTO do2dto(CouponRecipients item);

}
