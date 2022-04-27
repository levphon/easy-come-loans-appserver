package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.enums.GoodsType;
import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.converter.CouponConverter;
import cn.com.payu.app.modules.entity.Coupon;
import cn.com.payu.app.modules.entity.CouponRecipients;
import cn.com.payu.app.modules.entity.Goods;
import cn.com.payu.app.modules.mapper.CouponMapper;
import cn.com.payu.app.modules.mapper.CouponRecipientsMapper;
import cn.com.payu.app.modules.mapper.GoodsMapper;
import cn.com.payu.app.modules.model.CouponDTO;
import cn.com.payu.app.modules.model.CouponRecipientsDTO;
import cn.com.payu.app.modules.model.task.CloseCouponTask;
import cn.com.payu.app.modules.utils.AppContextHolder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.glsx.plat.common.utils.DateUtils;
import com.google.common.collect.Lists;
import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CouponService {

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private CouponRecipientsMapper couponRecipientsMapper;

    @Autowired
    private HashedWheelTimer hashedWheelTimer;

    public void receive(Long couponId) {
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new AppServerException("优惠券不存在");
        }

        List<CouponRecipients> recipientsList = couponRecipientsMapper.selectUnusedByUserIdAndCouponId(AppContextHolder.getUserId(), couponId);
        if (CollectionUtils.isNotEmpty(recipientsList)) {
            throw new AppServerException("您已经领取过抵用券了，快去开通会员吧！");
        } else {
            Date now = new Date();

            CouponRecipients recipients = new CouponRecipients();
            recipients.setUserId(AppContextHolder.getUserId());
            recipients.setCouponId(couponId);
            recipients.setCouponTitle(coupon.getTitle());
            recipients.setUsed(coupon.getUsed());
            recipients.setType(coupon.getType());
            recipients.setUsedAmount(coupon.getUsedAmount());
            recipients.setUsedQuantity(1);
            recipients.setUsedThreshold(1);
            recipients.setEffectiveDate(now);
            recipients.setExpirationDate(DateUtils.addDateMinutes(now, 15));
            recipients.setUsedFlag(0);
            recipients.setCreatedDate(now);
            couponRecipientsMapper.insert(recipients);

            //十五分钟后取消未使用会员抵用券
            hashedWheelTimer.newTimeout(new CloseCouponTask(recipients.getId(), couponRecipientsMapper), 15, TimeUnit.MINUTES);
        }
    }

    /**
     * 分配优惠券
     *
     * @param couponId
     * @param userId
     * @param num
     */
    public void assignCoupons(Long couponId, Long userId, Integer num) {
        if (num < 12) {
            return;
        }

        Coupon coupon = couponMapper.selectById(couponId);

        Date now = new Date();

        List<CouponRecipients> couponRecipientsList = Lists.newArrayList();

        // TODO: 2021/9/16 加入续费逻辑  如已经是会员，再买会员情况
        for (int i = 1; i <= num; i++) {
            CouponRecipients recipients = new CouponRecipients();
            recipients.setUserId(userId);
            recipients.setCouponId(couponId);
            recipients.setCouponTitle(coupon.getTitle());
            recipients.setUsed(coupon.getUsed());
            recipients.setType(coupon.getType());
            recipients.setUsedAmount(coupon.getUsedAmount());
            recipients.setUsedQuantity(1);
            recipients.setUsedThreshold(1);
            recipients.setEffectiveDate(DateUtils.addDateMonths(now, i - 1));
            recipients.setExpirationDate(DateUtils.addDateMonths(now, i));
            recipients.setUsedFlag(0);
            recipients.setCreatedDate(now);
            couponRecipientsList.add(recipients);
        }
        log.info(JSON.toJSONStringWithDateFormat(couponRecipientsList, DateUtils.NORMAL_DATE_TIME_PATTERN, SerializerFeature.WriteDateUseDateFormat));
        couponRecipientsMapper.insertList(couponRecipientsList);
    }

    public List<CouponDTO> list(Integer used) {
        if (GoodsType.VIP.getType().equals(used)) {
            List<CouponRecipients> receiveList = couponRecipientsMapper.selectUnusedByUserIdAndUsed(AppContextHolder.getUserId(), used);
            if (CollectionUtils.isNotEmpty(receiveList)) {
                return Lists.newArrayList();
            } else {
                if (AppContextHolder.isVip()) {
                    return Lists.newArrayList();
                }
                List<Coupon> itemList = couponMapper.selectByUsed(used);
                List<CouponDTO> list = Lists.newArrayList();
                itemList.forEach(item -> {
                    list.add(CouponConverter.INSTANCE.do2dto(item));
                });
                return list;
            }
        } else {
            throw new AppServerException("领取优惠券参数错误");
        }
    }

    public List<CouponRecipientsDTO> usableList(Integer used, Long goodsId) {
        List<CouponRecipients> itemList = couponRecipientsMapper.selectUnusedByUserIdAndUsed(AppContextHolder.getUserId(), used);
        List<CouponRecipientsDTO> list = Lists.newArrayList();
        itemList.forEach(item -> {
            if (validCouponRecipients(item, goodsId)) {
                CouponRecipientsDTO recipientsDTO = CouponConverter.INSTANCE.do2dto(item);
                recipientsDTO.setCouponId(item.getId());
                list.add(recipientsDTO);
            }
        });
        return list;
    }

    public boolean validCouponRecipients(CouponRecipients couponRecipients, Long goodsId) {
        if (couponRecipients.getUsedFlag() != 0) {
            return false;
        }

        if (!Objects.isNull(goodsId)) {
            Goods goods = goodsMapper.selectOnShelfById(goodsId);
            if (goods != null && goods.getPrice().compareTo(couponRecipients.getUsedAmount()) <= 0) {
                return false;
            }
        }

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(couponRecipients.getEffectiveDate());
        c2.setTime(couponRecipients.getExpirationDate());

        //截取日期
        Calendar c = DateUtils.truncate(Calendar.getInstance(), Calendar.DATE);
        c1 = DateUtils.truncate(c1, Calendar.DATE);
        c2 = DateUtils.truncate(c2, Calendar.DATE);
        return (c.after(c1) || c.equals(c1)) && (c.before(c2) || c.equals(c2)); //在有效期内
    }

    public List<CouponRecipientsDTO> have(Long userId) {
        List<CouponRecipients> itemList = couponRecipientsMapper.selectUnusedByUserId(userId);
        List<CouponRecipientsDTO> list = Lists.newArrayList();
        itemList.forEach(item -> {
            CouponRecipientsDTO recipientsDTO = CouponConverter.INSTANCE.do2dto(item);
            recipientsDTO.setCouponId(item.getId());
            list.add(recipientsDTO);
        });
        return list;
    }

}
