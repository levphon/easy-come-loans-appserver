package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.enums.GoodsType;
import cn.com.payu.app.common.enums.OrderStatus;
import cn.com.payu.app.modules.converter.OrderAddressConverter;
import cn.com.payu.app.modules.entity.Order;
import cn.com.payu.app.modules.entity.OrderAddress;
import cn.com.payu.app.modules.entity.User;
import cn.com.payu.app.modules.entity.UserLocalAccount;
import cn.com.payu.app.modules.mapper.*;
import cn.com.payu.app.modules.model.*;
import cn.com.payu.app.modules.model.export.MngOrderExport;
import cn.com.payu.app.modules.model.params.OrderSearch;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserLocalAccountMapper localAccountMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderGoodsMapper orderGoodsMapper;

    @Resource
    private OrderAddressMapper orderAddressMapper;

    @Resource
    private CouponRecipientsMapper couponRecipientsMapper;

    @Autowired
    private VipService vipService;

    @Autowired
    private LeChargeBizService leChargeBizService;

    public PageInfo<MngOrderDTO> search(OrderSearch search) {
        Page page = PageHelper.startPage(search.getPageNumber(), search.getPageSize());
        List<MngOrderDTO> list = orderMapper.search(search);
        return new PageInfo<>(list);
    }

    public List<MngOrderExport> export(OrderSearch search) {
        List<MngOrderExport> list = orderMapper.export(search);
        list.forEach(moe -> {
            moe.setOrderType(GoodsType.getTypeName(Integer.valueOf(moe.getOrderType())));
        });
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public void completeCharge(String orderNo, String appId) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null && order.getStatus().equals(OrderStatus.PAYING.getCode())) {
            orderMapper.updateSuccStatusById(order.getId(), appId);
            if (GoodsType.VIP.getType().equals(order.getOrderType())) {
                vipService.subscribe(order);
            } else if (GoodsType.GOODS.getType().equals(order.getOrderType())) {

            } else if (GoodsType.PHONE_COST.getType().equals(order.getOrderType())) {
                //这里只是完成订单的支付成功，还有乐充本身的充值订单发起充值，然后处理回调
                leChargeBizService.charge(order);
            }

            int updateCnt = couponRecipientsMapper.updateUsedFlagByOrderId(order.getId());
            log.info("修改订单{}券使用状态记录数{}", orderNo, updateCnt);
        }
    }

    public MngOrderDetailsDTO getById(Long id) {
        MngOrderDetailsDTO detailsDTO = null;

        Order order = orderMapper.selectById(id);
        if (order != null) {
            detailsDTO = new MngOrderDetailsDTO();

            User user = userMapper.selectByPrimaryKey(order.getCuserId());

            UserLocalAccount localAccount = localAccountMapper.selectByUserId(order.getCuserId());

            detailsDTO.setId(order.getId());
            detailsDTO.setOrderNo(order.getOrderNo());
            detailsDTO.setOrderType(order.getOrderType());
            detailsDTO.setAmount(order.getAmount());
            detailsDTO.setCreatedDate(order.getCreatedDate());

            detailsDTO.setRealName(localAccount != null ? localAccount.getUsername() : "");
            detailsDTO.setAccount(user.getAccount());
            detailsDTO.setChannel(user.getChannel());

            if (GoodsType.GOODS.getType().equals(order.getOrderType())) {
                OrderAddress orderAddress = orderAddressMapper.selectByOrderId(order.getId());
                detailsDTO.setOaInfo(OrderAddressConverter.INSTANCE.do2dto(orderAddress));
            }
        }
        return detailsDTO;
    }

    public OrderSummaryDTO getSummary() {
        List<OrderSummaryModel> totalSummary = orderMapper.selectSummary(null);
        List<OrderSummaryModel> todaySummary = orderMapper.selectSummary(DateUtils.format(new Date()));

        OrderSummaryDTO summaryDTO = new OrderSummaryDTO();
        summaryDTO.setTotalSummary(totalSummary);
        summaryDTO.setTodaySummary(todaySummary);
        return summaryDTO;
    }

    public List<MyOrderGoodsDTO> getPaidByOrderType(Integer orderType, Long userId) {
        return orderMapper.selectByOrderType(orderType, userId);
    }

}
