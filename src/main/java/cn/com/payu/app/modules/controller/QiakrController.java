package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.qiakr.resp.QiakrAuditResultResp;
import cn.com.payu.app.modules.service.QiakrBizService;
import cn.com.payu.app.modules.utils.AppContextHolder;
import cn.hutool.json.JSONUtil;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.core.web.R;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/qiakr")
@Api(value = "洽客产品模块", tags = {"洽客产品模块"})
public class QiakrController {

    @Autowired
    private QiakrBizService qiakrBizService;

    /**
     * 进件
     *
     * @return
     */
    @SysLog
    @GetMapping(value = "/apply")
    public R application() {
        qiakrBizService.application(AppContextHolder.getUserId());
        return R.ok();
    }

    /**
     * 进件结果
     *
     * @return
     */
    @SysLog
    @GetMapping(value = "/result")
    public R result() {
        //获取进件结果
        QiakrAuditResultResp resp = qiakrBizService.getResult(AppContextHolder.getUserId());

        Map<String, Object> rtnMap = Maps.newHashMap();
        rtnMap.put("status", 0);
        if (resp != null) {
            rtnMap.put("status", resp.getStatus());
            if (6002 == resp.getStatus() || "6002".equals(String.valueOf(resp.getStatus()))) {
                String url = qiakrBizService.getProductUrl(AppContextHolder.getUserId());
                rtnMap.put("url", url);
            } else if (6004 == resp.getStatus() || "6004".equals(String.valueOf(resp.getStatus()))) {
                rtnMap.put("remark", resp.getRemark());
            } else {
                log.info("用户{}在审核中:{}", AppContextHolder.getUserId(), JSONUtil.toJsonStr(resp));
            }
            return R.ok().data(rtnMap);
        }
        return R.ok().data(rtnMap);
    }

    /**
     * 产品url
     *
     * @return
     */
    @PostMapping(value = "/productUrl")
    public R getProductUrl() {
        String url = qiakrBizService.getProductUrl(AppContextHolder.getUserId());
        return R.ok().data(url);
    }

}
