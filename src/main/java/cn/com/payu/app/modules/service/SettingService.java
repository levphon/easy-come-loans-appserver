package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.converter.AppSettingConverter;
import cn.com.payu.app.modules.converter.UserFeedbackConverter;
import cn.com.payu.app.modules.entity.AppSetting;
import cn.com.payu.app.modules.entity.UserAuthRel;
import cn.com.payu.app.modules.entity.UserFeedback;
import cn.com.payu.app.modules.entity.UserSetting;
import cn.com.payu.app.modules.mapper.*;
import cn.com.payu.app.modules.model.AppSettingDTO;
import cn.com.payu.app.modules.model.UserFeedbackBO;
import cn.com.payu.app.modules.model.UserSettingBO;
import cn.com.payu.app.modules.utils.AppContextHolder;
import cn.com.payu.app.common.enums.AuthType;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SettingService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserAuthRelMapper authRelMapper;

    @Resource
    private UserLocalAuthMapper localAuthMapper;

    @Resource
    private UserThirdAuthMapper thirdAuthMapper;

    @Resource
    private UserLocalAccountMapper localAccountMapper;

    @Resource
    private UserProfileMapper userProfileMapper;

    @Resource
    private AppSettingMapper appSettingMapper;

    @Resource
    private UserSettingMapper userSettingMapper;

    @Resource
    private UserFeedbackMapper userFeedbackMapper;

    public List<AppSettingDTO> list(String type) {
        List<AppSetting> settingList = appSettingMapper.list(type);
        List<AppSettingDTO> list = Lists.newArrayList();
        settingList.forEach(cus -> {
            list.add(AppSettingConverter.INSTANCE.do2dto(cus));
        });
        return list;
    }

    public void setting(UserSettingBO settingBO) {
        UserSetting setting = userSettingMapper.selectByUserIdAndSettingId(AppContextHolder.getUserId(), settingBO.getSettingId());

        UserSetting userSetting = Optional.ofNullable(setting).orElse(new UserSetting());
        userSetting.setUserId(AppContextHolder.getUserId());
        userSetting.setSettingId(settingBO.getSettingId());
        userSetting.setItemValue(settingBO.getItemValue());
        userSettingMapper.updateByPrimaryKey(setting);
    }

    public void feedback(UserFeedbackBO feedbackBO) {
        UserFeedback feedback = UserFeedbackConverter.INSTANCE.bo2do(feedbackBO);
        feedback.setUserId(AppContextHolder.getUserId());
        feedback.setCreatedDate(new Date());
        userFeedbackMapper.insert(feedback);
    }

    public void destroyAccount() {
        Long userId = AppContextHolder.getUserId();

        userMapper.logicDeleteById(userId);

        localAccountMapper.logicDeleteByUserId(userId);

        userProfileMapper.logicDeleteByUserId(userId);

        List<UserAuthRel> authRelList = authRelMapper.selectByUserId(userId);
        authRelList.forEach(uar -> {
            if (AuthType.LOCAL.getType().equals(uar.getAuthType())) {
                localAuthMapper.logicDeleteById(userId);
            } else if (AuthType.THIRD.getType().equals(uar.getAuthType())) {
                thirdAuthMapper.logicDeleteById(userId);
            }
        });
        authRelMapper.logicDeleteByUserId(userId);
    }
    
}
