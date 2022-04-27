package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.ChannelDownload;
import cn.com.payu.app.modules.model.ChannelDownloadUserDTO;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChannelDownloadMapper extends CommonBaseMapper<ChannelDownload> {

    int countByChannelAndAccount(@Param("channel") String channel,
                                 @Param("account") String account,
                                 @Param("platform") String platform);

    List<ChannelDownload> selectEncryptAccount();

    List<ChannelDownloadUserDTO> selectDownloadUser();

    int updateDecryptAccount(@Param("id") Long id, @Param("decryptAccount") String decryptAccount);

}