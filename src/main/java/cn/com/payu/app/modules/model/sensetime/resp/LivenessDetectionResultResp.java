package cn.com.payu.app.modules.model.sensetime.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class LivenessDetectionResultResp extends AbstractSenseTimeResp {

    /**
     * 上传参数 接口中指定的参数liveness_mode
     */
    @JSONField(name = "liveness_mode")
    private String livenessMode;
    /**
     * 交互活体检测是否通过，true代表通过，false代表不通过
     */
    private Boolean passed;
    /**
     * 交互活体检测错误状态描述
     */
    @JSONField(name = "liveness_status")
    private String livenessStatus;
    /**
     * 上传参数 接口中指定的参数 biz_no，原样返回
     */
    @JSONField(name = "biz_no")
    private String bizNo;
    /**
     * 上传参数 接口中指定的参数 biztoken，原样返回
     */
    @JSONField(name = "biz_extra_data")
    private String bizExtraData;
    /**
     * base64编码后的原始帧图
     */
    @JSONField(name = "base64_liveness_image")
    private String base64LivenessImage;
    /**
     * 返回base64_liveness_image对应的数字签名信息，客户根据需求进行结果验证
     */
    @JSONField(name = "base64_liveness_image_sign")
    private String base64LivenessImageSign;
    /**
     * base64编码后的人脸裁剪图
     */
    @JSONField(name = "base64_liveness_face_image")
    private String base64LivenessFaceImage;
    /**
     * 返回base64_liveness_face_image对应的数字签名信息，客户根据需求进行结果验证
     */
    @JSONField(name = "base64_liveness_face_image_sign")
    private String base64LivenessFaceImageSign;

}
