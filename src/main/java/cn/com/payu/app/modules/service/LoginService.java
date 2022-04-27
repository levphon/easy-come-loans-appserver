package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.enums.AuthType;
import cn.com.payu.app.common.enums.LoginType;
import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.entity.*;
import cn.com.payu.app.modules.mapper.*;
import cn.com.payu.app.modules.model.*;
import cn.com.payu.app.modules.model.params.BindMobileBO;
import cn.com.payu.app.modules.model.params.QQLoginBO;
import cn.com.payu.app.modules.model.params.SettingChangePasswordBO;
import cn.com.payu.app.modules.utils.AppContextHolder;
import cn.com.payu.app.modules.utils.EncryptUtil;
import cn.com.payu.app.modules.utils.MTRandom;
import com.glsx.plat.common.utils.StringUtils;
import com.glsx.plat.redis.utils.RedisUtils;
import com.glsx.plat.web.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class LoginService {

    private static MTRandom mtRandom = new MTRandom();

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserAppMapper userAppMapper;

    @Resource
    private UserAuthRelMapper authRelMapper;

    @Resource
    private UserLocalAuthMapper localAuthMapper;

    @Resource
    private UserThirdAuthMapper thirdAuthMapper;

    @Resource
    private UserLocalAccountMapper localAccountMapper;

    @Resource
    private UserWechatAccountMapper wechatAccountMapper;

    @Resource
    private UserQQAccountMapper qqAccountMapper;

    @Autowired
    private SmsService smsService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 手机注册
     *
     * @param registerBO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public User register(MobileRegisterBO registerBO) {
        if (registerBO.getFromApp() == null) {
            registerBO.setFromApp(1);
        }
        User user = Optional.ofNullable(userMapper.selectByAccount(registerBO.getMobile(), registerBO.getFromApp())).orElse(new User());
        user.setAccount(registerBO.getMobile());
        user.setBlacklist(0);
        user.setChannel(StringUtils.isNotEmpty(registerBO.getChannel()) ? registerBO.getChannel() : "xinxinrong");
        user.setFromApp(registerBO.getFromApp());
        userMapper.saveOrUpdate(user);

        UserApp userApp = Optional.ofNullable(userAppMapper.selectByUserId(user.getId())).orElse(new UserApp());
        userApp.setUserId(user.getId());
        userApp.setClientId(registerBO.getClientId());
        userApp.setIp(IpUtils.getIpAddr(request));
        userAppMapper.saveOrUpdate(userApp);

        UserLocalAuth localAuth = new UserLocalAuth();
        localAuth.setMobile(registerBO.getMobile());
        String encryptedPasswd = bCryptPasswordEncoder.encode(registerBO.getPassword());
        localAuth.setPassword(encryptedPasswd);
        localAuthMapper.insertUseGeneratedKeys(localAuth);

        UserAuthRel userAuthRel = new UserAuthRel();
        userAuthRel.setUserId(user.getId());
        userAuthRel.setAuthId(localAuth.getId());
        userAuthRel.setAuthType(AuthType.LOCAL.getType());
        authRelMapper.insertUseGeneratedKeys(userAuthRel);

//        生成本地账号
        UserLocalAccount localAccount = new UserLocalAccount();
        localAccount.setUserId(user.getId());
        localAccount.setAuthId(localAuth.getId());

        // 2021/1/4 生成类似QQ的无规则账号
        String unionId = generateUnionId();
        log.info("新用户unionId为{}", unionId);

        localAccount.setUnionId(unionId);
        localAccount.setCreatedDate(new Date());
        localAccountMapper.insert(localAccount);

        redisUtils.set(localAccount.getUnionId(), user);
        return user;
    }

    public String generateUnionId() {
        //  1、redis 判断key是否存在 如果存在则用此算法重新生成（n+1），如果两次存在则换其他算法重新生成，3次存在则提示注册失败
        //  2、put到redis
        //  3、登录缓存用户
        int len = 6;
        String unionId = commonService.getRandom(len);
        //1、redis 判断key是否存在
        if (redisUtils.hasKey(unionId)) {
            log.warn("unionId【{}】已经存在", unionId);
            //如果存在则用此算法重新生成（n+1）
            unionId = commonService.getRandom(len + 1);
            //重新检测，如果两次存在则换其他算法重新生成
            if (redisUtils.hasKey(unionId)) {
                log.warn("unionId+1【{}】已经存在", unionId);
                unionId = String.valueOf(mtRandom.nextInt());
                if (redisUtils.hasKey(unionId)) {
                    log.warn("unionId+mt【{}】已经存在", unionId);
                    throw new AppServerException("注册失败，请稍后重试");
                }
            }
        }
        return unionId;
    }

    @Transactional(rollbackFor = Exception.class)
    public AppUser uLogin(LoginByMobileBO loginBO) {
        String mobile = EncryptUtil.PBEDecrypt(loginBO.getMobile());
        User user = userMapper.selectByAccount(EncryptUtil.PBEDecrypt(loginBO.getMobile()), loginBO.getFromApp());
        //本地登录
        if (user == null) {
            log.warn("{} 用户 {} 不存在", loginBO.getMobile(), mobile);
            return null;
        }

        UserAuthRel userAuthRel = authRelMapper.selectLocalAuthRelByUserId(user.getId());
        if (loginBO.getLoginType() == 1) {
            //密码登录
            UserLocalAuth localAuth = localAuthMapper.selectById(userAuthRel.getAuthId());
            if (localAuth == null) {
                throw new AppServerException("请先注册设置密码并完善资料");
            }
            boolean pwdMatched = bCryptPasswordEncoder.matches(loginBO.getCode(), localAuth.getPassword());
            if (!pwdMatched) {
                throw new AppServerException("账号或密码错误");
            }
        } else if (loginBO.getLoginType() == 2) {
            //验证码校验
            //smsService.verifyCode(loginBO.getMobile(), loginBO.getCode());
        } else if (loginBO.getLoginType() == 3) {

        } else {
            throw new AppServerException("登录类型参数错误");
        }
        AppUser appUser = localAccountMapper.selectAppUserByUserId(user.getId());
        return appUser;
    }

    @Transactional(rollbackFor = Exception.class)
    public AppUser localAuth(LoginByMobileBO loginBO) {
        if (loginBO.getFromApp() == null) {
            loginBO.setFromApp(1);
        }
        User user = userMapper.selectByAccount(loginBO.getMobile(), loginBO.getFromApp());
        //本地登录
        if (user == null) {
            MobileRegisterBO registerBO = new MobileRegisterBO();
            registerBO.setMobile(loginBO.getMobile());
            registerBO.setPassword("123456");
            registerBO.setFromApp(loginBO.getFromApp());
            user = register(registerBO);
        }
        if (user.getBlacklist() == 2) {
            throw new AppServerException(605, "您的账号被限制登录，请联系管理员！");
        }

        // TODO: 2021/9/16
//        UserApp userApp = userAppMapper.selectByUserId(user.getId());
//        //更新客户端id，用户有可能换手机登录
//        if (userApp != null && !loginBO.getClientId().equals(userApp.getClientId())) {
//            userAppMapper.updateClientIdByUserId(user.getId(), loginBO.getClientId());
//        }

//        InviteRegister register = inviteRegisterMapper.selectByInviteRegisterId(user.getId());
//        //邀请注册的用户只有手机号，必须先注册
//        if (register != null && register.getStatus() == 0) {
//            throw new AppServerException("请先注册设置密码并完善资料");
//        }

        UserAuthRel userAuthRel = authRelMapper.selectLocalAuthRelByUserId(user.getId());
        if (loginBO.getLoginType() == 1) {
            //密码登录
            UserLocalAuth localAuth = localAuthMapper.selectById(userAuthRel.getAuthId());
            if (localAuth == null) {
                throw new AppServerException("请先注册设置密码并完善资料");
            }
            boolean pwdMatched = bCryptPasswordEncoder.matches(loginBO.getCode(), localAuth.getPassword());
            if (!pwdMatched) {
                throw new AppServerException("账号或密码错误");
            }
        } else if (loginBO.getLoginType() == 2) {
            //验证码校验
            //smsService.verifyCode(loginBO.getMobile(), loginBO.getCode());
        } else {
            throw new AppServerException("登录类型参数错误");
        }
        AppUser appUser = localAccountMapper.selectAppUserByUserId(user.getId());
        return appUser;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserAuthRel qqAuth(QQOAuth2AccessToken accessToken) {
        UserThirdAuth thirdAuth = thirdAuthMapper.selectByOpenid(accessToken.getOpenId());
        if (thirdAuth == null) {
            thirdAuth = new UserThirdAuth();
            thirdAuth.setOpenid(accessToken.getOpenId());
            thirdAuth.setAccessToken(accessToken.getAccessToken());
            thirdAuth.setLoginType(LoginType.QQ.getType());
            thirdAuthMapper.insertUseGeneratedKeys(thirdAuth);
        }

        UserQQAccount qqAccount = qqAccountMapper.selectByAuthId(thirdAuth.getId());
        if (qqAccount == null) {
            qqAccount = new UserQQAccount();
            qqAccount.setAuthId(thirdAuth.getId());
            qqAccount.setUnionId(accessToken.getUnionId());
            qqAccountMapper.insertUseGeneratedKeys(qqAccount);
        }
        UserAuthRel authRel = authRelMapper.selectByAuthTypeAndAuthId(AuthType.THIRD.getType(), thirdAuth.getId());
        if (authRel == null) {
            authRel = new UserAuthRel();
            authRel.setAuthId(thirdAuth.getId());
            authRel.setAuthType(AuthType.THIRD.getType());
            authRelMapper.insertUseGeneratedKeys(authRel);
        }
        return authRel;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserAuthRel qqLogin(QQLoginBO loginBO) {
        UserThirdAuth thirdAuth = thirdAuthMapper.selectByOpenid(loginBO.getOpenId());
        if (thirdAuth == null) {
            thirdAuth = new UserThirdAuth();
            thirdAuth.setOpenid(loginBO.getOpenId());
//            thirdAuth.setAccessToken(accessToken.getAccessToken());
            thirdAuth.setLoginType(LoginType.QQ.getType());
            thirdAuthMapper.insertUseGeneratedKeys(thirdAuth);
        }

        UserQQAccount qqAccount = qqAccountMapper.selectByAuthId(thirdAuth.getId());
        if (qqAccount == null) {
            qqAccount = new UserQQAccount();
            qqAccount.setAuthId(thirdAuth.getId());
//            qqAccount.setUnionId(accessToken.getUnionId());
            qqAccountMapper.insertUseGeneratedKeys(qqAccount);
        }
        UserAuthRel authRel = authRelMapper.selectByAuthTypeAndAuthId(AuthType.THIRD.getType(), thirdAuth.getId());
        if (authRel == null) {
            authRel = new UserAuthRel();
            authRel.setAuthId(thirdAuth.getId());
            authRel.setAuthType(AuthType.THIRD.getType());
            authRelMapper.insertUseGeneratedKeys(authRel);
        }
        return authRel;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserAuthRel wechatAuth(WxOAuth2AccessToken accessToken) {
        UserThirdAuth thirdAuth = thirdAuthMapper.selectByOpenid(accessToken.getOpenId());
        if (thirdAuth == null) {
            thirdAuth = new UserThirdAuth();
            thirdAuth.setOpenid(accessToken.getOpenId());
            thirdAuth.setAccessToken(accessToken.getAccessToken());
            thirdAuth.setLoginType(LoginType.WECHAT.getType());
            thirdAuthMapper.insertUseGeneratedKeys(thirdAuth);
        }

        UserWechatAccount wechatAccount = wechatAccountMapper.selectByAuthId(thirdAuth.getId());
        if (wechatAccount == null) {
            wechatAccount = new UserWechatAccount();
            wechatAccount.setAuthId(thirdAuth.getId());
            wechatAccount.setUnionId(accessToken.getUnionId());
            wechatAccountMapper.insertUseGeneratedKeys(wechatAccount);
        }

        UserAuthRel authRel = authRelMapper.selectByAuthTypeAndAuthId(AuthType.THIRD.getType(), thirdAuth.getId());
        if (authRel == null) {
            authRel = new UserAuthRel();
            authRel.setAuthId(thirdAuth.getId());
            authRel.setAuthType(AuthType.THIRD.getType());
            authRelMapper.insertUseGeneratedKeys(authRel);
        }
        return authRel;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserAuthRel wechatLogin(WechatLoginBO loginBO) {
        UserThirdAuth thirdAuth = thirdAuthMapper.selectByOpenid(loginBO.getOpenId());
        if (thirdAuth == null) {
            thirdAuth = new UserThirdAuth();
            thirdAuth.setOpenid(loginBO.getOpenId());
            //thirdAuth.setAccessToken(accessToken.getAccessToken());
            thirdAuth.setLoginType(LoginType.WECHAT.getType());
            thirdAuthMapper.insertUseGeneratedKeys(thirdAuth);
        }

        UserWechatAccount wechatAccount = wechatAccountMapper.selectByAuthId(thirdAuth.getId());
        if (wechatAccount == null) {
            wechatAccount = new UserWechatAccount();
            wechatAccount.setAuthId(thirdAuth.getId());
            wechatAccount.setUnionId(loginBO.getUnionId());
            wechatAccountMapper.insertUseGeneratedKeys(wechatAccount);
        }

        UserAuthRel authRel = authRelMapper.selectByAuthTypeAndAuthId(AuthType.THIRD.getType(), thirdAuth.getId());
        if (authRel == null) {
            authRel = new UserAuthRel();
            authRel.setAuthId(thirdAuth.getId());
            authRel.setAuthType(AuthType.THIRD.getType());
            authRelMapper.insertUseGeneratedKeys(authRel);
        }
        return authRel;
    }

    @Transactional(rollbackFor = Exception.class)
    public AppUser bind(BindMobileBO bindMobileBO) {
        UserAuthRel authRel = authRelMapper.selectById(bindMobileBO.getAuthRelId());
        if (authRel == null) {
            throw new AppServerException("绑定账号发生错误");
        }

        User relUser = userMapper.selectById(authRel.getUserId());
        if (relUser != null) {
            throw new AppServerException("请勿重复绑定账号");
        }

        if (bindMobileBO.getFromApp() == null) {
            bindMobileBO.setFromApp(1);
        }

        //可能被邀请或注册中断，User可能存在
        User user = userMapper.selectByAccount(bindMobileBO.getMobile(), bindMobileBO.getFromApp());
        if (user == null) {
            //用户不存在，注册一个用户
            MobileRegisterBO registerBO = new MobileRegisterBO();
            registerBO.setMobile(bindMobileBO.getMobile());
            registerBO.setPassword(bindMobileBO.getPassword());
            registerBO.setFromApp(bindMobileBO.getFromApp());
            user = register(registerBO);
        } else {
            UserAuthRel userAuthRel = authRelMapper.selectLocalAuthRelByUserId(user.getId());
            UserLocalAuth localAuth = localAuthMapper.selectById(userAuthRel.getAuthId());
            boolean pwdMatched = bCryptPasswordEncoder.matches(bindMobileBO.getPassword(), localAuth.getPassword());
            if (!pwdMatched) {
                throw new AppServerException("账号密码错误");
            }
        }
        authRel.setUserId(user.getId());
        authRelMapper.updateByPrimaryKeySelective(authRel);

        AppUser appUser = localAccountMapper.selectAppUserByUserId(user.getId());
        return appUser;
    }

    @Transactional(rollbackFor = Exception.class)
    public void changePassword(SettingChangePasswordBO changePasswordBO) {
        UserLocalAccount localAccount = localAccountMapper.selectByUserId(AppContextHolder.getUserId());

        UserLocalAuth localAuth = localAuthMapper.selectById(localAccount.getAuthId());
        //验证码校验
        smsService.verifyCode(localAuth.getMobile(), changePasswordBO.getCode());

        String encryptedPasswd = bCryptPasswordEncoder.encode(changePasswordBO.getNewPassword());
        localAuthMapper.updatePasswordByUserId(AppContextHolder.getUserId(), encryptedPasswd);
    }

}
