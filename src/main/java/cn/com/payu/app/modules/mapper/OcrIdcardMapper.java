package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.OcrIdcard;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OcrIdcardMapper extends CommonBaseMapper<OcrIdcard> {

    OcrIdcard selectByUserId(@Param("userId") Long userId);

    int saveOrUpdate(OcrIdcard idcard);

}