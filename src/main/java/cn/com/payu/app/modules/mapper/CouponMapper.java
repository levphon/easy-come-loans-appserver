package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.Coupon;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CouponMapper extends CommonBaseMapper<Coupon> {

    Coupon selectById(@Param("id") Long id);

    List<Coupon> selectByUsed(@Param("used") Integer used);

}