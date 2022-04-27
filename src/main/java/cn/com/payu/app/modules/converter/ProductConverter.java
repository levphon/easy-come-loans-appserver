package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.Product;
import cn.com.payu.app.modules.model.MngProductBO;
import cn.com.payu.app.modules.model.MngProductInfoDTO;
import cn.com.payu.app.modules.model.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductConverter {

    ProductConverter INSTANCE = Mappers.getMapper(ProductConverter.class);

    ProductDTO do2dto(Product item);

    MngProductInfoDTO do2infoDto(Product item);

    Product bo2do(MngProductBO item);

}
