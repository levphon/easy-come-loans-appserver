package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.InviteRegister;
import cn.com.payu.app.modules.model.InviteRewardItemDTO;
import cn.com.payu.app.modules.model.InviteUserDTO;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InviteRegisterMapper extends CommonBaseMapper<InviteRegister> {

    InviteRegister selectByInviteRegisterId(@Param("registerId") Long registerId);

    List<InviteRegister> selectByUserId(@Param("userId") Long userId);

    List<InviteUserDTO> searchInviteUser(@Param("userId") Long userId);

    int selectInviteUserCnt(@Param("userId") Long userId);

    List<InviteRewardItemDTO> searchInviteReward(@Param("userId") Long userId);

    int updateStatusByRegisterId(@Param("registerId") Long registerId, @Param("status") Integer status);

}