package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.SensetimeLiveness;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SensetimeLivenessMapper extends CommonBaseMapper<SensetimeLiveness> {

    SensetimeLiveness selectByBizNo(@Param("bizNo") String bizNo);

    SensetimeLiveness selectByBizToken(@Param("bizToken") String bizToken);

    /**
     * 获取最后一次活体识别信息
     *
     * @param userId
     * @return
     */
    SensetimeLiveness selectLastLivenessByUserId(@Param("userId") Long userId);

    int saveOrUpdate(SensetimeLiveness liveness);

}