package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.CouponRecipients;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface CouponRecipientsMapper extends CommonBaseMapper<CouponRecipients> {

    CouponRecipients selectById(@Param("id") Long id);

    List<CouponRecipients> selectByUserId(@Param("userId") Long userId);

    List<CouponRecipients> selectUnusedByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);

    List<CouponRecipients> selectUnusedByUserId(@Param("userId") Long userId);

    List<CouponRecipients> selectUnusedByUserIdAndUsed(@Param("userId") Long userId, @Param("used") Integer used);

    int updateUsedFlagByIds(@Param("ids") Collection<Long> ids);

    int updateUsedFlagByOrderId(@Param("orderId") Long orderId);

    void closeCoupon(@Param("id") Long id);

}