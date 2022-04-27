package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.PaymentConfig;
import cn.com.payu.app.modules.model.MngPaymentDTO;
import cn.com.payu.app.modules.model.params.MngPaymentSearch;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentConfigMapper extends CommonBaseMapper<PaymentConfig> {

    PaymentConfig selectByAppId(@Param("appId") String appId);

    List<PaymentConfig> selectAllConfig();

    PaymentConfig selectUsedConfig();

    List<MngPaymentDTO> search(MngPaymentSearch search);

    int updateUnusedStatus(@Param("status") Integer status);

    int updateUsedStatusById(@Param("id") Long id, @Param("status") Integer status);

    int updatePayCntByAppId(@Param("appId") String appId, @Param("payCnt") Integer payCnt);

}