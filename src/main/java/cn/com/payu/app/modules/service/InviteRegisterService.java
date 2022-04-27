package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.entity.ChannelDownload;
import cn.com.payu.app.modules.entity.EventTracking;
import cn.com.payu.app.modules.entity.User;
import cn.com.payu.app.modules.mapper.ChannelDownloadMapper;
import cn.com.payu.app.modules.mapper.EventTrackingMapper;
import cn.com.payu.app.modules.mapper.UserMapper;
import cn.com.payu.app.modules.model.InviteDownloadBO;
import cn.com.payu.app.modules.model.InviteRegisterBO;
import cn.com.payu.app.modules.model.MobileRegisterBO;
import cn.com.payu.app.modules.utils.EncryptUtil;
import com.glsx.plat.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class InviteRegisterService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Resource
    private EventTrackingMapper eventTrackingMapper;

    @Resource
    private ChannelDownloadMapper channelDownloadMapper;

    /**
     * 邀请注册
     *
     * @param registerBO
     */
    @Transactional(rollbackFor = Exception.class)
    public User inviteRegister(InviteRegisterBO registerBO) {
        if (registerBO.getFromApp() == null) {
            registerBO.setFromApp(1);
        }
        User checkedUser = userService.checkExistUser(registerBO.getMobile(), registerBO.getFromApp());
        if (checkedUser == null) {
            MobileRegisterBO register = new MobileRegisterBO();
            register.setMobile(registerBO.getMobile());
            register.setPassword("123456");
            register.setChannel(registerBO.getInviteCode());
            register.setFromApp(registerBO.getFromApp());
            User user = loginService.register(register);
            return user;
        }
        return checkedUser;
    }

    public void recordDownload(InviteDownloadBO downloadBO) {
        String channel = downloadBO.getInviteCode();
        String account = downloadBO.getMobile();
        if (StringUtils.isNotEmpty(channel)) {
            int cnt = channelDownloadMapper.countByChannelAndAccount(channel, account, downloadBO.getType());
            if (cnt <= 0) {
                String decryptAccount = EncryptUtil.PBEDecrypt(account);

                ChannelDownload download = new ChannelDownload();
                download.setChannel(channel);
                download.setPlatform(downloadBO.getType());
                download.setAccount(account);
                download.setDecryptAccount(decryptAccount);
                download.setStatus(1);
                download.setCreatedDate(new Date());
                channelDownloadMapper.insert(download);

                User user = userMapper.selectByAccount(decryptAccount != null ? decryptAccount : account, 1);

                EventTracking tracking = new EventTracking();
                tracking.setChannel(channel);
                tracking.setCuserId(user != null ? user.getId() : 0L);
                tracking.setTriggerType("downloadStr");
                tracking.setPageUrl("/invite/download");
                tracking.setUrl("/invite/download");
                tracking.setEntryTime(new Date());
                tracking.setLeaveTime(null);
                tracking.setStayTime(null);
                tracking.setCreatedDate(new Date());
                eventTrackingMapper.insert(tracking);
            }
        }
    }

}
