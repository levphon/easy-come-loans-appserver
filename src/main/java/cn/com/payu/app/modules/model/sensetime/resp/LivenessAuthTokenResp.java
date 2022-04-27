package cn.com.payu.app.modules.model.sensetime.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class LivenessAuthTokenResp extends AbstractSenseTimeResp {

    /**
     * 当次H5交互活体检测的biztoken，有效时长为30min
     */
    @JSONField(name = "biztoken")
    private String bizToken;
    /**
     * Linux时间戳，标识biztoken的过期时间
     */
    @JSONField(name = "expiration_time")
    private Long expirationTime;
    /**
     * H5交互活体检测页面URL，格式为https://h5.visioncloudapi.com?biztoken='xxx'
     */
    @JSONField(name = "liveness_detection_url")
    private String livenessDetectionUrl;

}
