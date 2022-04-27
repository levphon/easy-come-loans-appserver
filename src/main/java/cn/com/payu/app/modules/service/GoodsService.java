package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.enums.GoodsType;
import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.converter.GoodsConverter;
import cn.com.payu.app.modules.entity.Goods;
import cn.com.payu.app.modules.entity.OrderAddress;
import cn.com.payu.app.modules.entity.OrderChargeItem;
import cn.com.payu.app.modules.mapper.GoodsMapper;
import cn.com.payu.app.modules.mapper.OrderAddressMapper;
import cn.com.payu.app.modules.mapper.OrderChargeItemMapper;
import cn.com.payu.app.modules.model.GoodsDTO;
import cn.com.payu.app.modules.model.GoodsDetailsDTO;
import cn.com.payu.app.modules.model.params.BuyGoodsBO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private OrderAddressMapper orderAddressMapper;

    @Resource
    private OrderChargeItemMapper orderChargeItemMapper;

    @Autowired
    private PaymentService paymentService;

    public List<GoodsDTO> list(Integer type) {
        List<Goods> itemList = goodsMapper.selectByType(type);
        List<GoodsDTO> list = Lists.newArrayList();
        itemList.forEach(item -> {
            list.add(GoodsConverter.INSTANCE.do2dto(item));
        });
        return list;
    }

    public GoodsDetailsDTO getById(Long id) {
        Goods goods = goodsMapper.selectOnShelfById(id);
        return GoodsConverter.INSTANCE.do2detailDto(goods);
    }

    @Transactional(rollbackFor = Exception.class)
    public Object buy(BuyGoodsBO buyGoodsBO) {
        Object result = paymentService.prepay(buyGoodsBO);
        if (result != null) {
            Goods goods = goodsMapper.selectOnShelfById(buyGoodsBO.getGoodsId());
            if (goods == null) {
                throw new AppServerException("该商品已下架");
            }
            if (GoodsType.GOODS.getType().equals(goods.getGoodsType())) {
                OrderAddress orderAddress = new OrderAddress();
                orderAddress.setOrderId(buyGoodsBO.getOrderId());
                orderAddress.setName(buyGoodsBO.getName());
                orderAddress.setTel(buyGoodsBO.getPhone());
                orderAddress.setAddress(buyGoodsBO.getAddress());
                orderAddress.setCreatedDate(new Date());
                orderAddressMapper.insert(orderAddress);
            } else if (GoodsType.YOUHUI_BUY.getType().equals(goods.getGoodsType())) {

            } else if (GoodsType.CREDIT_REPORT.getType().equals(goods.getGoodsType())) {

            } else if (GoodsType.PHONE_COST.getType().equals(goods.getGoodsType())) {
                OrderChargeItem chargeItem = new OrderChargeItem();
                chargeItem.setOrderId(buyGoodsBO.getOrderId());
                chargeItem.setAccount(buyGoodsBO.getPhone());
                chargeItem.setStatus("0");
                chargeItem.setCreatedDate(new Date());
                orderChargeItemMapper.insert(chargeItem);
            } else {
                throw new AppServerException("商品参数错误，请联系客服！");
            }
        }
        return result;
    }

}
