package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.converter.ProductConverter;
import cn.com.payu.app.modules.entity.Product;
import cn.com.payu.app.modules.entity.ProductDrainage;
import cn.com.payu.app.modules.mapper.ProductDrainageMapper;
import cn.com.payu.app.modules.mapper.ProductMapper;
import cn.com.payu.app.modules.model.*;
import cn.com.payu.app.modules.model.export.MngProductExport;
import cn.com.payu.app.modules.model.params.MngProductSettingBO;
import cn.com.payu.app.modules.model.params.ProductSearch;
import cn.com.payu.app.modules.utils.AppContextHolder;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.core.enums.SysConstants;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductDrainageMapper productDrainageMapper;

    public List<ProductDTO> list() {
        List<Product> itemList = productMapper.selectUsable();
        List<ProductDTO> list = Lists.newArrayList();
        itemList.forEach(item -> {
//            if (AppContextHolder.isVip()) {//会员可见全部产品
//                list.add(ProductConverter.INSTANCE.do2dto(item));
//            } else {
//                //非会员可见常规产品
//                if (item.getType() == 0) {
//                    list.add(ProductConverter.INSTANCE.do2dto(item));
//                }
//            }
            list.add(ProductConverter.INSTANCE.do2dto(item));
        });
        return list;
    }

    public String click(Long productId) {

        Product product = productMapper.selectById(productId);

        if (product == null || product.getEnableStatus() == 2) {
            throw new AppServerException("借款渠道不存在或已下架");
        }

        if (!AppContextHolder.isVip() && product.getType() == 1) {
            throw new AppServerException("先去开通会员吧，更高额度及各种权益等着您！");
        }

        productMapper.increaseUsedCntById(productId);

        ProductDrainage drainage = new ProductDrainage();
        drainage.setChannel(product.getChannel());
        drainage.setProductId(productId);
        drainage.setUserId(AppContextHolder.getUserId());
        drainage.setStatus(1);
        drainage.setCreatedDate(new Date());
        productDrainageMapper.insert(drainage);

        return product.getUrl();
    }



}
