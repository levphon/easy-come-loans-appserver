package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.MngGoodsBO;
import cn.com.payu.app.modules.model.MngGoodsDTO;
import cn.com.payu.app.modules.model.MngGoodsInfoDTO;
import cn.com.payu.app.modules.model.params.GoodsSearch;
import cn.com.payu.app.modules.service.MngGoodsService;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.common.enums.OperateType;
import com.glsx.plat.context.utils.validator.AssertUtils;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/mng/goods")
@Api(value = "后台商品管理模块", tags = {"后台商品管理模块"})
public class MngGoodsController {

    private final static String MODULE = "商品管理";

    @Autowired
    private MngGoodsService mngGoodsService;

    @ApiOperation("商品列表查询")
    @GetMapping("/search")
    public R search(@Valid GoodsSearch search) {
        PageInfo<MngGoodsDTO> pageInfo = mngGoodsService.search(search);
        return R.ok().putPageData(pageInfo);
    }

    @SysLog(module = MODULE, action = OperateType.ADD)
    @ApiOperation("新增商品")
    @PostMapping(value = "/add")
    public R add(@RequestBody @Validated MngGoodsBO goodsBO) {
        mngGoodsService.add(goodsBO);
        return R.ok();
    }

    @GetMapping("/info")
    public R info(@RequestParam("id") Long id) {
        MngGoodsInfoDTO infoDTO = mngGoodsService.getInfoById(id);
        return R.ok().data(infoDTO);
    }

    @SysLog(module = MODULE, action = OperateType.EDIT)
    @ApiOperation("编辑商品")
    @PostMapping(value = "/edit")
    public R edit(@RequestBody @Validated MngGoodsBO goodsBO) {
        AssertUtils.isNull(goodsBO.getId(), "ID不能为空");
        mngGoodsService.edit(goodsBO);
        return R.ok();
    }

    @ApiOperation("商品上下架")
    @GetMapping("/uplow")
    public R uplow(@RequestParam("id") Long id) {
        int uplow = mngGoodsService.uplow(id);
        return R.ok().data(uplow);
    }

}
