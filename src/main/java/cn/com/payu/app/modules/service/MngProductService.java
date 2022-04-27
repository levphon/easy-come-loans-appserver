package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.converter.ProductConverter;
import cn.com.payu.app.modules.entity.Product;
import cn.com.payu.app.modules.mapper.ProductMapper;
import cn.com.payu.app.modules.model.MngProductBO;
import cn.com.payu.app.modules.model.MngProductDTO;
import cn.com.payu.app.modules.model.MngProductInfoDTO;
import cn.com.payu.app.modules.model.MngProductSettingDTO;
import cn.com.payu.app.modules.model.export.MngProductExport;
import cn.com.payu.app.modules.model.params.MngProductSettingBO;
import cn.com.payu.app.modules.model.params.ProductSearch;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.core.enums.SysConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class MngProductService {

    @Resource
    private ProductMapper productMapper;

    public PageInfo<MngProductDTO> search(ProductSearch search) {
        Page page = PageHelper.startPage(search.getPageNumber(), search.getPageSize());
        List<MngProductDTO> list = productMapper.search(search);
        return new PageInfo<>(list);
    }

    public List<MngProductExport> export(ProductSearch search) {
        List<MngProductExport> list = productMapper.export(search);
        return list;
    }

    public void add(MngProductBO productBO) {
        int cnt = productMapper.countByName(productBO.getName());
        if (cnt > 0) {
            throw new AppServerException("该产品已存在！");
        }
        Product product = ProductConverter.INSTANCE.bo2do(productBO);
        product.setEnableStatus(SysConstants.EnableStatus.disable.getCode());
        product.setCreatedDate(new Date());
        productMapper.insert(product);
    }

    public void edit(MngProductBO productBO) {
        Product existProduct = productMapper.selectByName(productBO.getName());
        if (existProduct != null && !existProduct.getId().equals(productBO.getId())) {
            throw new AppServerException(productBO.getName() + "产品已存在！");
        }

        Product product = ProductConverter.INSTANCE.bo2do(productBO);
        product.setUpdatedDate(new Date());
        productMapper.updateByPrimaryKeySelective(product);
    }

    public MngProductSettingDTO getSetting(Long productId) {
        Product product = productMapper.selectById(productId);
        if (product != null) {
            MngProductSettingDTO settingDTO = new MngProductSettingDTO();
            settingDTO.setProductId(product.getId());
            settingDTO.setShelfTime(product.getShelfTime());
            settingDTO.setOffShelfTime(product.getOffShelfTime());
            settingDTO.setMaxDailyUV(product.getMaxDailyUV());
            settingDTO.setMaxTotalUV(product.getMaxTotalUV());
            return settingDTO;
        }
        return null;
    }

    public void setting(MngProductSettingBO settingBO) {
        Product product = productMapper.selectById(settingBO.getProductId());
        if (product != null) {
            product.setShelfTime(settingBO.getShelfTime());
            product.setOffShelfTime(settingBO.getOffShelfTime());
            product.setMaxDailyUV(settingBO.getMaxDailyUV());
            product.setMaxTotalUV(settingBO.getMaxTotalUV());
            productMapper.updateByPrimaryKey(product);
        }
    }

    public MngProductInfoDTO getInfoById(Long id) {
        Product product = productMapper.selectById(id);
        MngProductInfoDTO infoDTO = null;
        if (product != null) {
            infoDTO = ProductConverter.INSTANCE.do2infoDto(product);
        }
        return infoDTO;
    }

    public int uplow(Long id) {
        Product product = productMapper.selectById(id);
        if (Objects.equals(product.getEnableStatus(), SysConstants.EnableStatus.enable.getCode())) {
            productMapper.updateEnableStatusById(id, SysConstants.EnableStatus.disable.getCode());
            return SysConstants.EnableStatus.disable.getCode();
        } else if (Objects.equals(product.getEnableStatus(), SysConstants.EnableStatus.disable.getCode())) {
            productMapper.updateEnableStatusById(id, SysConstants.EnableStatus.enable.getCode());
            return SysConstants.EnableStatus.enable.getCode();
        }
        return 0;
    }

}
