package cn.com.payu.app.modules.model.qiakr.req;

import lombok.Data;

@Data
public abstract class AbstractQiakrRequest {

    private String channelId;
    private Long timestamp;
    private String sign;
    private String request;

}
