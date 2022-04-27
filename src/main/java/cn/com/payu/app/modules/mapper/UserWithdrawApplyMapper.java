package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.UserWithdrawApply;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserWithdrawApplyMapper extends CommonBaseMapper<UserWithdrawApply> {

    int updateWithdrawApplyStatus(UserWithdrawApply apply);

}