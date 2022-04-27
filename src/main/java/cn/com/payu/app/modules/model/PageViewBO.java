package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class PageViewBO {

    private String triggerType;

    private PageInfoModel pageInfo;

    private Long entryTime;
    private Long leaveTime;
    private Long stayTime;

    private SystemInfoModel systemInfo;

}
