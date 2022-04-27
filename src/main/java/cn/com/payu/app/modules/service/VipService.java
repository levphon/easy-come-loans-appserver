package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.converter.VipConverter;
import cn.com.payu.app.modules.entity.*;
import cn.com.payu.app.modules.mapper.*;
import cn.com.payu.app.modules.model.VipDTO;
import com.glsx.plat.common.utils.DateUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VipService {

    @Resource
    private VipMapper vipMapper;

    @Resource
    private UserVipMapper userVipMapper;

    @Resource
    private UserVipHistoryMapper userVipHistoryMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Autowired
    private CouponService couponService;

    public List<VipDTO> list() {
        List<Vip> vipList = vipMapper.selectList();

        List<VipDTO> list = Lists.newArrayList();
        vipList.forEach(vip -> {
            list.add(VipConverter.INSTANCE.do2dto(vip));
        });
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public void subscribe(Order order) {
        Goods goods = goodsMapper.selectByOrderId(order.getId());
        Vip vip = vipMapper.selectById(goods.getRefId());
        if (vip != null) {
            Date now = new Date();
            //修改用户vip状态
            UserVip userVip = Optional.ofNullable(userVipMapper.selectByUserId(order.getCuserId())).orElse(new UserVip());
            userVip.setUserId(order.getCuserId());
            boolean userVipFlag = this.isValidVip(userVip);
            if (userVipFlag) {
                //如果会员有效，验证会员有效期即可，状态改为续费，延长对应会员月份
                userVip.setStatus(3);
                userVip.setExpirationDate(DateUtils.addDateMonths(userVip.getExpirationDate(), vip.getDuring()));
            } else {
                //如果会员已经失效，更新状态和起止时间
                userVip.setStatus(1);
                userVip.setEffectiveDate(now);
                userVip.setExpirationDate(DateUtils.addDateMonths(now, vip.getDuring()));
            }
            userVip.setLevel(1);
            userVipMapper.saveOrUpdate(userVip);

            // 2021/2/22 保存开通会员历史记录
            UserVipHistory userVipHistory = new UserVipHistory();
            userVipHistory.setUserId(order.getCuserId());
            userVipHistory.setType(0);
            userVipHistory.setVipId(vip.getId());
            userVipHistory.setActualCost(vip.getPrice());
            userVipHistory.setQuantity(vip.getDuring());
            userVipHistory.setEffectiveDate(DateUtils.addDateMonths(userVip.getExpirationDate(), -vip.getDuring()));
            userVipHistory.setExpirationDate(userVip.getExpirationDate());
            userVipHistory.setStatus(userVip.getStatus());
            userVipHistory.setCreatedDate(now);
            userVipHistoryMapper.insert(userVipHistory);

            //开通会员成功
            couponService.assignCoupons(2L, order.getCuserId(), vip.getDuring());
        }
    }

    public UserVip getUserVipByUserId(Long userId) {
        return userVipMapper.selectByUserId(userId);
    }

    public UserVip getUserVipInfo(Long userId) {
        UserVip userVip = userVipMapper.selectByUserId(userId);
        if (userVip == null) {
            userVip = new UserVip();
            userVip.setLevel(0);
            userVip.setStatus(0);
            return userVip;
        }
        //会员等级，第一版本不搞，先默认1，负数为失效的会员等级
        userVip.setLevel(1);
        if (!isValidVip(userVip)) {
            userVip.setLevel(-userVip.getLevel());
            //定时任务更新会员状态可能存在时间间隙
            if (userVip.getStatus() == 1 || userVip.getStatus() == 3) {
                userVip.setStatus(2);
            }
        }
        return userVip;
    }

    public boolean isValidVip(Long userId) {
        UserVip userVip = userVipMapper.selectByUserId(userId);
        return isValidVip(userVip);
    }

    public boolean isValidVip(UserVip userVip) {
        Optional<UserVip> optUserVip = Optional.ofNullable(userVip);
        if (optUserVip.isPresent()) {
            UserVip vip = optUserVip.get();
            if (vip.getStatus() == null || vip.getStatus() == 0 || vip.getStatus() == 2) {
                return false;
            }
            Calendar c = Calendar.getInstance();
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(vip.getEffectiveDate());
            c2.setTime(vip.getExpirationDate());
            boolean vipFlag = (c.after(c1) || c.equals(c1)) && (c.before(c2) || c.equals(c2)); //在有效期内
            if (!vipFlag) {
                userVipMapper.updateStatusByUserId(userVip.getUserId(), 2);
            }
            return vipFlag;
        }
        return false;
    }

}
