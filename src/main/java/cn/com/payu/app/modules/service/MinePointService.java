package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.entity.EventTracking;
import cn.com.payu.app.modules.mapper.EventTrackingMapper;
import cn.com.payu.app.modules.model.PageViewBO;
import cn.com.payu.app.modules.utils.AppContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class MinePointService {

    @Resource
    private EventTrackingMapper eventTrackingMapper;

    public void upv(PageViewBO pageViewBO) {
        EventTracking tracking = new EventTracking();
//        tracking.setChannel(AppContextHolder.getChannel());
//        tracking.setCuserId(AppContextHolder.getUserId());
        tracking.setTriggerType(pageViewBO.getTriggerType());
        tracking.setPageUrl(pageViewBO.getPageInfo().getPageUrl());
        tracking.setUrl(pageViewBO.getPageInfo().getUrl());
        tracking.setEntryTime(pageViewBO.getEntryTime() != null ? new Date(pageViewBO.getEntryTime()) : null);
        tracking.setLeaveTime(pageViewBO.getLeaveTime() != null ? new Date(pageViewBO.getLeaveTime()) : null);
        tracking.setStayTime(pageViewBO.getStayTime());
        tracking.setCreatedDate(new Date());
        eventTrackingMapper.insert(tracking);
    }

}
