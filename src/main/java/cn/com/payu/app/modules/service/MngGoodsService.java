package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.converter.GoodsConverter;
import cn.com.payu.app.modules.entity.Goods;
import cn.com.payu.app.modules.mapper.GoodsMapper;
import cn.com.payu.app.modules.model.MngGoodsBO;
import cn.com.payu.app.modules.model.MngGoodsDTO;
import cn.com.payu.app.modules.model.MngGoodsInfoDTO;
import cn.com.payu.app.modules.model.params.GoodsSearch;
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
public class MngGoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    public PageInfo<MngGoodsDTO> search(GoodsSearch search) {
        Page page = PageHelper.startPage(search.getPageNumber(), search.getPageSize());
        List<MngGoodsDTO> list = goodsMapper.search(search);
        return new PageInfo<>(list);
    }

    public void add(MngGoodsBO goodsBO) {
        int cnt = goodsMapper.countByTypeAndCode(goodsBO.getGoodsType(), goodsBO.getGoodsCode());
        if (cnt > 0) {
            throw new AppServerException("该商品已存在！");
        }
        Goods goods = GoodsConverter.INSTANCE.bo2do(goodsBO);
        goods.setEnableStatus(SysConstants.EnableStatus.disable.getCode());
        goods.setCreatedDate(new Date());
        goodsMapper.insert(goods);
    }

    public MngGoodsInfoDTO getInfoById(Long id) {
        Goods goods = goodsMapper.selectById(id);
        MngGoodsInfoDTO infoDTO = null;
        if (goods != null) {
            infoDTO = GoodsConverter.INSTANCE.do2infoDto(goods);
        }
        return infoDTO;
    }

    public void edit(MngGoodsBO goodsBO) {
        Goods existGoods = goodsMapper.selectByTypeAndCode(goodsBO.getGoodsType(), goodsBO.getGoodsCode());
        if (existGoods != null && !existGoods.getId().equals(goodsBO.getId())) {
            throw new AppServerException(goodsBO.getName() + "商品已存在！");
        }
        Goods goods = GoodsConverter.INSTANCE.bo2do(goodsBO);
        goods.setUpdatedDate(new Date());
        goodsMapper.updateByPrimaryKeySelective(goods);
    }

    public int uplow(Long id) {
        Goods goods = goodsMapper.selectById(id);
        if (Objects.equals(goods.getEnableStatus(), SysConstants.EnableStatus.enable.getCode())) {
            goodsMapper.updateEnableStatusById(id, SysConstants.EnableStatus.disable.getCode());
            return SysConstants.EnableStatus.disable.getCode();
        } else if (Objects.equals(goods.getEnableStatus(), SysConstants.EnableStatus.disable.getCode())) {
            goodsMapper.updateEnableStatusById(id, SysConstants.EnableStatus.enable.getCode());
            return SysConstants.EnableStatus.enable.getCode();
        }
        return 0;
    }

}
