package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.mapper.EventTrackingMapper;
import cn.com.payu.app.modules.mapper.UserMapper;
import cn.com.payu.app.modules.mapper.UserProfileMapper;
import cn.com.payu.app.modules.model.ApplySummaryDTO;
import cn.com.payu.app.modules.model.ClickSummaryDTO;
import cn.com.payu.app.modules.model.RegisterSummaryDTO;
import cn.com.payu.app.modules.model.params.SummarySearch;
import com.glsx.plat.common.utils.DateUtils;
import com.glsx.plat.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class HomeService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserProfileMapper userProfileMapper;

    @Resource
    private EventTrackingMapper eventTrackingMapper;

    public ClickSummaryDTO clickSummary(SummarySearch search) {
        setSearchCondition(search);
        return eventTrackingMapper.selectStatCnt(search);
    }

    public RegisterSummaryDTO registerSummary(SummarySearch search) {
        setSearchCondition(search);
        return userMapper.selectStatCnt(search);
    }

    public ApplySummaryDTO applySummary(SummarySearch search) {
        setSearchCondition(search);
        return userProfileMapper.selectStatCnt(search);
    }

    private void setSearchCondition(SummarySearch search) {
        if (StringUtils.isNullOrEmpty(search.getSDate()) || StringUtils.isNullOrEmpty(search.getEDate())) {
            search.setSDate(DateUtils.format(new Date()));
            search.setEDate(DateUtils.format(new Date()));
        }
    }

}
