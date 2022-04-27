package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.UserBankCard;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserBankCardMapper extends CommonBaseMapper<UserBankCard> {

    UserBankCard selectById(@Param("id") Long id);

    UserBankCard selectByUserBankCard(UserBankCard bankCard);

    List<UserBankCard> selectByUserId(@Param("userId") Long userId);

}