package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_event_clicking")
public class EventClicking extends BaseEntity {


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

}