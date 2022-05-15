package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.User;
import cn.com.payu.app.modules.model.ClickSummaryDTO;
import cn.com.payu.app.modules.model.RegisterSummaryDTO;
import cn.com.payu.app.modules.model.params.SummarySearch;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends CommonBaseMapper<User> {

    User selectById(@Param("id") Long id);

    User selectByAccount(@Param("account") String account);

    User selectByPhoneMd5(@Param("phoneMd5") String phoneMd5);

    int saveOrUpdate(User user);

    int logicDeleteById(@Param("id") Long id);

    RegisterSummaryDTO selectStatCnt(SummarySearch search);

}