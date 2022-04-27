package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.Vip;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VipMapper extends CommonBaseMapper<Vip> {

    Vip selectById(@Param("id") Long id);

    List<Vip> selectList();

}