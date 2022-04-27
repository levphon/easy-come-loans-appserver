package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.Goods;
import cn.com.payu.app.modules.model.GoodsDTO;
import cn.com.payu.app.modules.model.GoodsDetailsDTO;
import cn.com.payu.app.modules.model.MngGoodsBO;
import cn.com.payu.app.modules.model.MngGoodsInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GoodsConverter {

    GoodsConverter INSTANCE = Mappers.getMapper(GoodsConverter.class);

    GoodsDTO do2dto(Goods item);

    GoodsDetailsDTO do2detailDto(Goods item);

    MngGoodsInfoDTO do2infoDto(Goods item);

    Goods bo2do(MngGoodsBO item);

}
