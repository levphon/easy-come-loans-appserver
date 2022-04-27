package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.UserFeedback;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFeedbackMapper extends CommonBaseMapper<UserFeedback> {

    UserFeedback selectById(@Param("id") Long id);

    List<UserFeedback> selectByUserId(@Param("userId") Long userId);

}