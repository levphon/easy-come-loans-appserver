package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.*;
import cn.com.payu.app.modules.model.export.MngChannelExport;
import cn.com.payu.app.modules.model.params.ChannelSearch;
import cn.com.payu.app.modules.model.params.MngChannelSettingBO;
import cn.com.payu.app.modules.service.ChannelService;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.common.enums.OperateType;
import com.glsx.plat.common.utils.DateUtils;
import com.glsx.plat.context.utils.PropertiesUtils;
import com.glsx.plat.context.utils.validator.AssertUtils;
import com.glsx.plat.core.web.R;
import com.glsx.plat.office.excel.EasyExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/mng/channel")
@Api(value = "后台渠道管理模块", tags = {"后台渠道管理模块"})
public class MngChannelController {

    private final static String MODULE = "渠道管理";

    @Autowired
    private ChannelService channelService;

    @ApiOperation("渠道列表查询")
    @GetMapping("/search")
    public R search(ChannelSearch search) {
        PageInfo<MngChannelDTO> pageInfo = channelService.search(search);
        return R.ok().putPageData(pageInfo);
    }

    @ApiOperation("渠道列表导出")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, ChannelSearch search) throws Exception {
        List<MngChannelExport> list = channelService.export(search);
        EasyExcelUtils.writeExcel(response, list, "渠道_" + DateUtils.formatSerial(new Date()), "Sheet1", MngChannelExport.class);
    }

    @ApiOperation("渠道列表查询")
    @GetMapping("/options")
    public R options() {
        List<SimpleChannelDTO> list = channelService.optionList();
        return R.ok().data(list);
    }

    @SysLog(module = MODULE, action = OperateType.ADD)
    @ApiOperation("新增渠道")
    @PostMapping(value = "/add")
    public R add(@RequestBody @Validated MngChannelBO channelBO) {
        channelService.add(channelBO);
        return R.ok();
    }

    @GetMapping("/info")
    public R info(@RequestParam("id") Long id) {
        MngChannelInfoDTO infoDTO = channelService.getInfoById(id);
        return R.ok().data(infoDTO);
    }

    @SysLog(module = MODULE, action = OperateType.EDIT)
    @ApiOperation("编辑渠道")
    @PostMapping(value = "/edit")
    public R edit(@RequestBody @Validated MngChannelBO channelBO) {
        AssertUtils.isNull(channelBO.getId(), "ID不能为空");
        channelService.edit(channelBO);
        return R.ok();
    }

    @ApiOperation("获取渠道配置")
    @GetMapping(value = "/getsetting")
    public R getSetting(@RequestParam("channelId") Long channelId) {
        MngChannelSettingDTO settingDTO = channelService.getSetting(channelId);
        return R.ok().data(settingDTO);
    }

    @ApiOperation("渠道配置")
    @PostMapping(value = "/setting")
    public R setting(@RequestBody MngChannelSettingBO settingBO) {
        channelService.setting(settingBO);
        return R.ok();
    }

    @ApiOperation("渠道上下架")
    @GetMapping("/uplow")
    public R uplow(@RequestParam("id") Long id) {
        int uplow = channelService.uplow(id);
        return R.ok().data(uplow);
    }

    @ApiOperation("渠道推广链接")
    @GetMapping(value = "/geturl")
    public R<String> getUrl(@RequestParam("channel") String channel) {
        String url = PropertiesUtils.getProperty("share.channel.url") + channel;
        return R.ok().data(url);
    }

}
