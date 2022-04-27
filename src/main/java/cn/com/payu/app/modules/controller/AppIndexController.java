package cn.com.payu.app.modules.controller;

import cn.com.payu.app.common.enums.GoodsType;
import cn.com.payu.app.modules.entity.UserVip;
import cn.com.payu.app.modules.model.MyOrderGoodsDTO;
import cn.com.payu.app.modules.model.UserLoanDTO;
import cn.com.payu.app.modules.model.UserProfileDTO;
import cn.com.payu.app.modules.model.params.UserLoanBO;
import cn.com.payu.app.modules.service.OrderService;
import cn.com.payu.app.modules.service.UserService;
import cn.com.payu.app.modules.service.VipService;
import cn.com.payu.app.modules.utils.AppContextHolder;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.common.enums.OperateType;
import com.glsx.plat.core.web.R;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/index")
@Api(value = "App首页", tags = {"App首页"})
public class AppIndexController {

    private final static String MODULE = "App首页";

    @Autowired
    private UserService userService;

    @Autowired
    private VipService vipService;

    @Autowired
    private OrderService orderService;

    @SysLog(module = MODULE, action = OperateType.SETTING)
    @PostMapping(value = "/loan")
    public R loan(@RequestBody @Validated UserLoanBO loanBO) {
        userService.setUserLoan(loanBO);
        return R.ok();
    }

    @GetMapping(value = "/getLoan")
    public R getLoan() {
        UserLoanDTO loanDTO = userService.getUserLoan(AppContextHolder.getUserId());
        return R.ok().data(loanDTO);
    }

    @GetMapping(value = "/index")
    public R isCertificated() {
        UserProfileDTO profile = userService.getUserProfileInfo(AppContextHolder.getUserId());
        boolean isVip = vipService.isValidVip(AppContextHolder.getUserId());
        Integer status = 0;//待认证
        if (profile != null) {
            status = 1;//待审核
            ZonedDateTime nowTime = ZonedDateTime.now();
            if (!isVip) {
//                ZonedDateTime beginTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(profile.getRegisterDate().toInstant().toEpochMilli()), ZoneId.systemDefault());
//                Duration duration = Duration.between(nowTime, beginTime).abs();
//                if (duration.toHours() >= 3) {
                    status = 2;//非会员超3小时
//                }
            } else {
                UserVip userVip = vipService.getUserVipByUserId(AppContextHolder.getUserId());
                ZonedDateTime beginTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(userVip.getEffectiveDate().toInstant().toEpochMilli()), ZoneId.systemDefault());
                Duration duration = Duration.between(nowTime, beginTime).abs();
                if (duration.toHours() >= (15 * 24)) {
                    status = 3;//7天后 匹配到符合条件产品
                }
            }
        }

        List<MyOrderGoodsDTO> yhgos = orderService.getPaidByOrderType(GoodsType.YOUHUI_BUY.getType(), AppContextHolder.getUserId());
        MyOrderGoodsDTO yhgo = CollectionUtils.isNotEmpty(yhgos) ? yhgos.get(0) : null;
        List<MyOrderGoodsDTO> zxbgs = orderService.getPaidByOrderType(GoodsType.CREDIT_REPORT.getType(), AppContextHolder.getUserId());
        MyOrderGoodsDTO zxbg = CollectionUtils.isNotEmpty(zxbgs) ? zxbgs.get(0) : null;

        Map<String, Object> rtnMap = Maps.newHashMap();
        rtnMap.put("isVip", isVip);
        rtnMap.put("profile", profile);
        rtnMap.put("buyTxka", yhgo);
        rtnMap.put("buyZxbg", zxbg);
        rtnMap.put("status", status);
        return R.ok().data(rtnMap);
    }

}
