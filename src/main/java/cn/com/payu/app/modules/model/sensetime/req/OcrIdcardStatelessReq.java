package cn.com.payu.app.modules.model.sensetime.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.File;

@Data
public class OcrIdcardStatelessReq extends AbstractSenseTimeReq {

    /**
     * 图片的文件
     */
    @JSONField(name = "image_file")
    private File imageFile;

    /**
     * 经过base64编码的图片文件
     */
    @JSONField(name = "image_base64")
    private String imageBase64;

    /**
     * 默认值为false，不旋转；值为 true 时，对图片进行自动旋转
     */
    @JSONField(name = "auto_rotate")
    private String autoRotate;

    /**
     * 用于说明idcard正反面，默认值为auto表示自动，值为front表示正面（人像面），值为back表示背面（即为国徽面），
     */
    @JSONField(name = "side")
    private String side;

    /**
     * 默认值为false；值为 true 时，返回身份证来源类型
     */
    @JSONField(name = "classify")
    private String classify;

    /**
     * 默认值为false；值为 true 时，返回身份证各字段置信度
     */
    @JSONField(name = "return_score")
    private String returnScore;

}
