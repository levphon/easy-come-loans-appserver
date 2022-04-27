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
import cn.com.payu.app.modules.utils.idcardUtil.IdcardInfoExtractor;
import cn.hutool.core.lang.UUID;
import com.glsx.plat.common.utils.DateUtils;
import com.glsx.plat.common.utils.ObjectUtils;
import com.glsx.plat.common.utils.StringUtils;
import com.glsx.plat.context.utils.PropertiesUtils;
import com.glsx.plat.jwt.base.ComJwtUser;
import com.glsx.plat.jwt.util.JwtUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Resource
    private OcrIdcardMapper ocrIdcardMapper;

    @Autowired
    private VipService vipService;

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
        //完善资料
        UserProfile profile = userProfileMapper.selectByUserId(AppContextHolder.getUserId());
        if (profile != null) {
            throw new AppServerException("请勿重复提交资料");
        }

        Date now = new Date();

        profile = new UserProfile();
        BeanUtils.copyProperties(profileBO, profile);
        profile.setUserId(AppContextHolder.getUserId());
        profile.setCreatedDate(now);
        userProfileMapper.insertUseGeneratedKeys(profile);

        if (StringUtils.isNotEmpty(profileBO.getFirstName())) {
            CustUserContact contact = new CustUserContact();
            contact.setUserId(AppContextHolder.getUserId());
            contact.setName(profileBO.getFirstName());
            contact.setPhone(profileBO.getFirstPhone());
            contact.setRelation(profileBO.getFirstRelation());
            contact.setRelationCode(profileBO.getFirstRelationCode());
            contact.setLevel(1);
            contact.setCreatedDate(now);
            custUserContactMapper.insert(contact);
        }
        if (StringUtils.isNotEmpty(profileBO.getSecondName())) {
            CustUserContact contact = new CustUserContact();
            contact.setUserId(AppContextHolder.getUserId());
            contact.setName(profileBO.getSecondName());
            contact.setPhone(profileBO.getSecondPhone());
            contact.setRelation(profileBO.getSecondRelation());
            contact.setRelationCode(profileBO.getSecondRelationCode());
            contact.setLevel(2);
            contact.setCreatedDate(now);
            custUserContactMapper.insert(contact);
        }

        IdcardInfoExtractor ie = new IdcardInfoExtractor(profileBO.getIdentityNo());

        OcrIdcard idcard = ocrIdcardMapper.selectByUserId(AppContextHolder.getUserId());
        if (idcard == null) {
            idcard = new OcrIdcard();
        }
        idcard.setUserId(AppContextHolder.getUserId());
        idcard.setCid(profileBO.getIdentityNo());
        idcard.setName(profileBO.getRealName());
        idcard.setGender(ie.getZhGender());
        idcard.setBirthday(DateUtils.format(ie.getBirthday()));

        String path = PropertiesUtils.getProperty("app.img.url");
        idcard.setFrontUrl(path + profileBO.getIdCardFront());
        idcard.setBackUrl(path + profileBO.getIdCardBack());
        ocrIdcardMapper.saveOrUpdate(idcard);

        UserLocalAccount localAccount = localAccountMapper.selectByUserId(AppContextHolder.getUserId());
        localAccount.setUsername(profileBO.getRealName());
        //localAccount.setAvatar(profileBO.getAvatar());
        localAccountMapper.updateByPrimaryKeySelective(localAccount);

        //邀请注册用户处理，注册完成
        inviteRegisterMapper.updateStatusByRegisterId(AppContextHolder.getUserId(), 1);

        //没完善资料没返给前端，这里需要返回一下
        AppContextHolder.getUser().setUsername(localAccount.getUsername());
        AppContextHolder.getUser().setAvatar(localAccount.getAvatar());
        AppContextHolder.getUser().setProfileId(profile.getId());
        log.info("AppUser {} info is {}", localAccount.getUnionId(), AppContextHolder.getUser());

        return AppContextHolder.getUser();
    }

    public User checkExistUser(String mobile, Integer fromApp) {
        //用户可能只填了号码没完成后面注册流程，或是被邀请用户只留了号码，不能仅判断User是否存在
        User user = userMapper.selectByAccount(mobile, fromApp);
        if (user != null) {
            //本地账号已经有了，表示已经注册
//            UserLocalAccount localAccount = localAccountMapper.selectByUserId(user.getId());
//            if (localAccount != null) {
//                throw new AppServerException("该手机号已经被注册，请直接登录");
//            }
        }
        return user;
    }

    public User getById(Long userId) {
        return userMapper.selectById(userId);
    }

    /**
     * 生成带用户信息的token
     *
     * @param user
     * @return
     */
    public String createToken(User user) {
        Assert.notNull(user, "获取当前用户信息失败");
        String uuid = UUID.randomUUID().toString(); //JWT 随机ID,做为验证的key
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
     * 生成带用户信息的token
     *
     * @param user
     * @return
     */
    public String createToken(AppUser user) {
        Assert.notNull(user, "获取当前用户信息失败");
        String uuid = UUID.randomUUID().toString(); //JWT 随机ID,做为验证的key
        String jwtId = jwtUtils.getApplication() + ":" + uuid + "_" + jwtUtils.JWT_SESSION_PREFIX + user.getId();
        ComJwtUser jwtUser = new ComJwtUser();
        jwtUser.setApplication(jwtUtils.getApplication());
        jwtUser.setJwtId(jwtId);
        jwtUser.setBelong("APP" + user.getFromApp());
        jwtUser.setUserId(String.valueOf(user.getId()));
        jwtUser.setAccount(user.getAccount());
        Map<String, Object> userMap = (Map<String, Object>) ObjectUtils.objectToMap(jwtUser);
        return jwtUtils.createToken(userMap);
    }

    public IndividualPage individualPage(Long userId) {
        UserLocalAccount localAccount = localAccountMapper.selectByUserId(userId);
        User user = userMapper.selectById(userId);

        //用户资料
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile == null) {
            return null;
        }

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        BeanUtils.copyProperties(userProfile, userProfileDTO);
        userProfileDTO.setUsername(localAccount.getUsername());
        userProfileDTO.setAvatar(localAccount.getAvatar());
        userProfileDTO.setRegisterDate(userProfile.getCreatedDate());

        //会员
        UserVip userVip = vipService.getUserVipInfo(userId);
        IndividualPage page = new IndividualPage();
        page.setVip(userVip.getLevel());
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
        bankCardList.forEach(card -> {
            list.add(UserBankCardConverter.INSTANCE.do2dto(card));
        });
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeMobile(SettingChangeMobileBO changeMobileBO) {
        Optional<User> optUser = Optional.ofNullable(userMapper.selectByAccount(changeMobileBO.getMobile(), AppContextHolder.getFromApp()));
        if (optUser.isPresent()) {
            User user = optUser.get();
            if (!user.getId().equals(AppContextHolder.getUserId())) {
                throw new AppServerException("该手机号已经被注册");
            } else {
                //号码是原号码，直接返回
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
