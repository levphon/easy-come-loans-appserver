package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.Goods;
import cn.com.payu.app.modules.model.MngGoodsDTO;
import cn.com.payu.app.modules.model.params.GoodsSearch;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper extends CommonBaseMapper<Goods> {

    Goods selectById(@Param("id") Long id);

    Goods selectOnShelfById(@Param("id") Long id);

    Goods selectByOrderId(@Param("orderId") Long orderId);

    List<Goods> selectByType(@Param("type") Integer type);

    List<MngGoodsDTO> search(GoodsSearch search);

    Goods selectByTypeAndCode(@Param("type") Integer type, @Param("code") String code);

    int countByTypeAndCode(@Param("type") Integer type, @Param("code") String code);

    int updateEnableStatusById(@Param("id") Long id, @Param("enableStatus") Integer enableStatus);

}