package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.SniffUnionLogin;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SniffUnionLoginMapper extends CommonBaseMapper<SniffUnionLogin> {

    SniffUnionLogin selectByPhoneMd5(@Param("phoneMd5") String phoneMd5);

}