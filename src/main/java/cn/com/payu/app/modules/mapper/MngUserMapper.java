package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.MngUser;
import cn.com.payu.app.modules.model.params.MngUserSearch;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MngUserMapper extends CommonBaseMapper<MngUser> {

    List<MngUser> selectList(MngUserSearch search);

    MngUser selectById(@Param("id") Long id);

    MngUser selectByAccount(@Param("account") String account);

    int selectCntByAccount(@Param("account") String username);

    int logicDeleteById(@Param("id") Long id);

}