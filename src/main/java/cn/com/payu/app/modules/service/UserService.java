package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.enums.AuthType;
import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.converter.UserBankCardConverter;
import cn.com.payu.app.modules.converter.UserConverter;
import cn.com.payu.app.modules.entity.*;
import cn.com.payu.app.modules.mapper.*;
import cn.com.payu.app.modules.model.*;
import cn.com.payu.app.modules.model.params.BindBankCardBO;
import cn.com.payu.app.modules.model.params.SettingChangeMobileBO;
import cn.com.payu.app.modules.model.params.UserLoanBO;
import cn.com.payu.app.modules.utils.AppContextHolder;
import cn.hutool.core.lang.UUID;
import com.glsx.plat.common.utils.ObjectUtils;
import com.glsx.plat.jwt.base.ComJwtUser;
import com.glsx.plat.jwt.util.JwtUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Resource
    private JwtUtils<ComJwtUser> jwtUtils;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserAuthRelMapper authRelMapper;
    @Resource
    private UserLocalAuthMapper localAuthMapper;
    @Resource
    private UserThirdAuthMapper thirdAuthMapper;
    @Resource
    private UserProfileMapper userProfileMapper;
    @Resource
    private UserLocalAccountMapper localAccountMapper;
    @Resource
    private UserWechatAccountMapper wechatAccountMapper;
    @Resource
    private UserQQAccountMapper qqAccountMapper;
    @Resource
    private InviteRegisterMapper inviteRegisterMapper;
    @Resource
    private UserBankCardMapper bankCardMapper;
    @Resource
    private CustUserContactMapper custUserContactMapper;

    public UserThirdAuth getUserThirdAuthByOpenid(String openid) {
        return thirdAuthMapper.selectByOpenid(openid);
    }

    public UserLocalAccount getLocalAccountByUserId(Long userId) {
        return localAccountMapper.selectByUserId(userId);
    }

    public UserWechatAccount getWechatAccountByAuthId(Long authId) {
        return wechatAccountMapper.selectByAuthId(authId);
    }

    public UserQQAccount getQQAccountByAuthId(Long authId) {
        return qqAccountMapper.selectByAuthId(authId);
    }

    public AppUser getAppUserByUserId(Long userId) {
        return localAccountMapper.selectAppUserByUserId(userId);
    }

    public UserProfile getUserProfile(Long userId) {
        UserProfile profile = userProfileMapper.selectByUserId(userId);
        return profile;
    }

    public UserProfileDTO getUserProfileInfo(Long userId) {
        UserProfile profile = userProfileMapper.selectByUserId(userId);
        UserProfileDTO profileDTO = UserConverter.INSTANCE.do2dto(profile);
        if (profileDTO != null) {
            profileDTO.setAvatar(AppContextHolder.getUser().getAvatar());
            profileDTO.setUsername(AppContextHolder.getUser().getUsername());
            profileDTO.setRegisterDate(profile.getCreatedDate());
        }
        return profileDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public AppUser fillProfile(UserProfileBO profileBO) {
        //????????????
        UserProfile profile = userProfileMapper.selectByUserId(AppContextHolder.getUserId());
        if (profile != null) {
            throw new AppServerException("????????????????????????");
        }

        Date now = new Date();

        profile = new UserProfile();
        BeanUtils.copyProperties(profileBO, profile);
        profile.setUserId(AppContextHolder.getUserId());
        profile.setCreatedDate(now);
        userProfileMapper.insertUseGeneratedKeys(profile);

        UserLocalAccount localAccount = localAccountMapper.selectByUserId(AppContextHolder.getUserId());
        localAccount.setUsername(profileBO.getRealName());
        //localAccount.setAvatar(profileBO.getAvatar());
        localAccountMapper.updateByPrimaryKeySelective(localAccount);

        //???????????????????????????????????????
        inviteRegisterMapper.updateStatusByRegisterId(AppContextHolder.getUserId(), 1);

        //?????????????????????????????????????????????????????????
        AppContextHolder.getUser().setUsername(localAccount.getUsername());
        AppContextHolder.getUser().setAvatar(localAccount.getAvatar());
        AppContextHolder.getUser().setProfileId(profile.getId());
        log.info("AppUser {} info is {}", localAccount.getUnionId(), AppContextHolder.getUser());

        return AppContextHolder.getUser();
    }

    public User checkExistUser(String mobile) {
        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????User????????????
        User user = userMapper.selectByAccount(mobile);
        if (user != null) {
            //?????????????????????????????????????????????
//            UserLocalAccount localAccount = localAccountMapper.selectByUserId(user.getId());
//            if (localAccount != null) {
//                throw new AppServerException("?????????????????????????????????????????????");
//            }
        }
        return user;
    }

    public User getById(Long userId) {
        return userMapper.selectById(userId);
    }

    /**
     * ????????????????????????token
     *
     * @param user
     * @return
     */
    public String createToken(User user) {
        Assert.notNull(user, "??????????????????????????????");
        String uuid = UUID.randomUUID().toString(); //JWT ??????ID,???????????????key
        String jwtId = jwtUtils.getApplication() + ":" + uuid + "_" + jwtUtils.JWT_SESSION_PREFIX + user.getId();
        ComJwtUser jwtUser = new ComJwtUser();
        jwtUser.setApplication(jwtUtils.getApplication());
        jwtUser.setJwtId(jwtId);
        jwtUser.setUserId(String.valueOf(user.getId()));
        jwtUser.setAccount(user.getAccount());
        Map<String, Object> userMap = (Map<String, Object>) ObjectUtils.objectToMap(jwtUser);
        return jwtUtils.createToken(userMap);
    }

    /**
     * ????????????????????????token
     *
     * @param user
     * @return
     */
    public String createToken(AppUser user) {
        Assert.notNull(user, "??????????????????????????????");
        String uuid = UUID.randomUUID().toString(); //JWT ??????ID,???????????????key
        String jwtId = jwtUtils.getApplication() + ":" + uuid + "_" + jwtUtils.JWT_SESSION_PREFIX + user.getId();
        ComJwtUser jwtUser = new ComJwtUser();
        jwtUser.setApplication(jwtUtils.getApplication());
        jwtUser.setJwtId(jwtId);
        //jwtUser.setBelong("APP" + user.getFromApp());
        jwtUser.setUserId(String.valueOf(user.getId()));
        jwtUser.setAccount(user.getAccount());
        Map<String, Object> userMap = (Map<String, Object>) ObjectUtils.objectToMap(jwtUser);
        return jwtUtils.createToken(userMap);
    }

    public IndividualPage individualPage(Long userId) {
        UserLocalAccount localAccount = localAccountMapper.selectByUserId(userId);

        //????????????
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile == null) {
            return null;
        }

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        BeanUtils.copyProperties(userProfile, userProfileDTO);
        userProfileDTO.setAccount(AppContextHolder.getAccount());
        userProfileDTO.setUsername(localAccount.getUsername());
        userProfileDTO.setAvatar(localAccount.getAvatar());
        userProfileDTO.setRegisterDate(userProfile.getCreatedDate());

        IndividualPage page = new IndividualPage();
        page.setUser(userProfileDTO);
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    public void bindBankCard(BindBankCardBO bindBO) {
        UserBankCard bankCard = bankCardMapper.selectByUserBankCard(new UserBankCard()
                .setUserId(AppContextHolder.getUserId())
                .setAccountName(bindBO.getAccountName())
                .setAccountNo(bindBO.getAccountNo()));
        if (bankCard != null) {
            return;
        }

        bankCard = UserBankCardConverter.INSTANCE.bo2do(bindBO);
        bankCard.setUserId(AppContextHolder.getUserId());
        bankCardMapper.insert(bankCard);
    }

    public List<BindBankCardDTO> getBindBankCards() {
        List<UserBankCard> bankCardList = bankCardMapper.selectByUserId(AppContextHolder.getUserId());
        List<BindBankCardDTO> list = Lists.newArrayList();
        bankCardList.forEach(card -> list.add(UserBankCardConverter.INSTANCE.do2dto(card)));
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeMobile(SettingChangeMobileBO changeMobileBO) {
        Optional<User> optUser = Optional.ofNullable(userMapper.selectByAccount(changeMobileBO.getMobile()));
        if (optUser.isPresent()) {
            User user = optUser.get();
            if (!user.getId().equals(AppContextHolder.getUserId())) {
                throw new AppServerException("???????????????????????????");
            } else {
                //?????????????????????????????????
                return;
            }
        }
        localAuthMapper.updateMobileByUserId(AppContextHolder.getUserId(), changeMobileBO.getMobile());
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

    public void setUserLoan(UserLoanBO loanBO) {
        UserProfile userProfile = userProfileMapper.selectByUserId(AppContextHolder.getUserId());
        if (userProfile != null) {
            userProfile.setLoanPeriod(loanBO.getLoanPeriod());
            userProfile.setLoanAmount(loanBO.getLoanAmount());
            userProfileMapper.updateByPrimaryKeySelective(userProfile);
        }
    }

    public UserLoanDTO getUserLoan(Long userId) {
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile != null) {
            UserLoanDTO loanDTO = new UserLoanDTO();
            loanDTO.setLoanPeriod(userProfile.getLoanPeriod());
            loanDTO.setLoanAmount(userProfile.getLoanAmount());
            return loanDTO;
        }
        return null;
    }

}
