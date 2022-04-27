package cn.com.payu.app.modules.model.sensetime.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class LivenessAuthTokenReq extends AbstractSenseTimeReq {

    @JSONField(name = "redirect_url")
    private String redirectUrl;

    @JSONField(name = "is_show_default_result_page")
    private Boolean isShowDefaultResultPage;

    @JSONField(name = "biz_no")
    private String bizNo;

    @JSONField(name = "biz_extra_data")
    private String bizExtraData;

    @JSONField(name = "web_title")
    private String webTitle;

    @JSONField(name = "liveness_mode")
    private String livenessMode;

    @JSONField(name = "return_face_image")
    private Boolean returnFaceImage;

    @JSONField(name = "return_image")
    private Boolean returnImage;

}
