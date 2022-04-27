package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_event_tracking")
public class EventTracking extends BaseEntity {

    /**
     * 渠道
     */
    private String channel;

    /**
     * 用户id
     */
    @Column(name = "cuser_id")
    private Long cuserId;

    /**
     * 事件类型
     */
    @Column(name = "trigger_type")
    private String triggerType;

    /**
     * 页面url，不带参数
     */
    @Column(name = "page_url")
    private String pageUrl;

    /**
     * 页面url，带参数
     */
    private String url;

    /**
     * 进入页面时间
     */
    @Column(name = "entry_time")
    private Date entryTime;

    /**
     * 离开页面时间
     */
    @Column(name = "leave_time")
    private Date leaveTime;

    /**
     * 页面停留时长
     */
    @Column(name = "stay_time")
    private Long stayTime;

}