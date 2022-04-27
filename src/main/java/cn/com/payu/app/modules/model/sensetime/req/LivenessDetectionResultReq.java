package cn.com.payu.app.modules.model.sensetime.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class LivenessDetectionResultReq extends AbstractSenseTimeReq {

    @JSONField(name = "biztoken")
    private String biztoken;

    @JSONField(name = "request_id")
    private String requestId;

}
