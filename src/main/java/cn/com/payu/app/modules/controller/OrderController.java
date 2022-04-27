package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.MyOrderGoodsDTO;
import cn.com.payu.app.modules.service.OrderService;
import cn.com.payu.app.modules.utils.AppContextHolder;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/order")
@Api(value = "我的订单", tags = {"我的订单"})
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/getPaidByOrderType")
    public R getPaidByOrderType(@RequestParam("orderType") Integer orderType) {
        List<MyOrderGoodsDTO> list = orderService.getPaidByOrderType(orderType, AppContextHolder.getUserId());
        return R.ok().data(list);
    }

}
