package cn.com.payu.app.modules.model.sensetime.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class AbstractSenseTimeResp {

    @JSONField(name = "request_id")
    private String requestId;
    private String code;
    private String message;

}
