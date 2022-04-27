package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.SysIdRecord;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysIdRecordMapper extends CommonBaseMapper<SysIdRecord> {

    SysIdRecord selectBySysId(@Param("sysId") String sysId);

    SysIdRecord selectBySysNameAndBizName(@Param("sysName") String sysName, @Param("bizName") String bizName);

}