package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.CustUserContact;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustUserContactMapper extends CommonBaseMapper<CustUserContact> {

    List<CustUserContact> selectByUserId(@Param("userId") Long userId);

}