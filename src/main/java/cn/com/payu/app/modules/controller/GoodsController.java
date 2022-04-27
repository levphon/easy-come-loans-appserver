package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.GoodsDTO;
import cn.com.payu.app.modules.model.GoodsDetailsDTO;
import cn.com.payu.app.modules.model.params.BuyGoodsBO;
import cn.com.payu.app.modules.service.GoodsService;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/goods")
@Api(value = "商品模块", tags = {"商品模块"})
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping(value = "/list")
    public R list(@RequestParam("type") Integer type) {
        List<GoodsDTO> items = goodsService.list(type);
        return R.ok().data(items);
    }

    @GetMapping(value = "/info")
    public R info(@RequestParam("id") Long id) {
        GoodsDetailsDTO item = goodsService.getById(id);
        return R.ok().data(item);
    }

    /**
     * 购买商品
     *
     * @param buyGoodsBO
     * @return
     */
    @ApiOperation(value = "购买商品")
    @PostMapping(value = "/buy")
    public R buy(@RequestBody @Validated BuyGoodsBO buyGoodsBO) {
        log.info("购买商品参数：{}", buyGoodsBO.toString());
        Object result = goodsService.buy(buyGoodsBO);
        return R.ok().data(result);
    }

}
