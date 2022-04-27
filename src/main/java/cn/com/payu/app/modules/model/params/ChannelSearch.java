package cn.com.payu.app.modules.model.params;

import cn.hutool.db.Page;
import lombok.Data;

import java.util.Collection;

@Data
public class ChannelSearch extends Page {

    private String sDate;
    private String eDate;
    private String channel;
    private String channelName;
    private Collection<Long> channelIds;

}
