package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.UserCertification;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserCertificationMapper extends CommonBaseMapper<UserCertification> {

    UserCertification selectByUserId(@Param("userId") Long userId);

}