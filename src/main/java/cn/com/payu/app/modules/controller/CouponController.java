package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.CouponDTO;
import cn.com.payu.app.modules.model.CouponRecipientsDTO;
import cn.com.payu.app.modules.service.CouponService;
import cn.com.payu.app.modules.utils.AppContextHolder;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/coupon")
@Api(value = "优惠券模块", tags = {"优惠券模块"})
public class CouponController {

    @Autowired
    private CouponService couponService;

    @ApiOperation(value = "可领取优惠券查询接口")
    @GetMapping(value = "/list")
    public R list(@RequestParam("used") Integer used) {
        List<CouponDTO> list = couponService.list(used);
        return R.ok().data(list);
    }

    @ApiOperation(value = "领取优惠券接口")
    @GetMapping(value = "/receive")
    public R receive(@RequestParam("couponId") Long couponId) {
        couponService.receive(couponId);
        return R.ok();
    }

    @ApiOperation(value = "可用优惠券查询接口")
    @GetMapping(value = "/usable")
    public R usable(@RequestParam("used") Integer used, @RequestParam(value = "goodsId", required = false) Long goodsId) {
        List<CouponRecipientsDTO> list = couponService.usableList(used, goodsId);
        return R.ok().data(list);
    }

    @ApiOperation(value = "已有优惠券接口")
    @GetMapping(value = "/have")
    public R have() {
        List<CouponRecipientsDTO> list = couponService.have(AppContextHolder.getUserId());
        return R.ok().data(list);
    }

}
