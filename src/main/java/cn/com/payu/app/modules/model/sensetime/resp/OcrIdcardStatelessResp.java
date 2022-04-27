package cn.com.payu.app.modules.model.sensetime.resp;

import lombok.Data;

@Data
public class OcrIdcardStatelessResp extends AbstractSenseTimeResp {

    /**
     * front 代表身份证正面，back 代表身份证反面（即国徽面）
     */
    private String side;
    /**
     * 身份证文字信息
     */
    private OcrIdcardStatelessInfo info;
    /**
     * 各项信息有效性（注：该字段目前无效，不建议使用）
     */
    private OcrIdcardStatelessValidity validity;
    /**
     * 各项信息识别结果的置信度，分数越高，准确度越高。取值范围是0到1之间的浮点数，一般场景下建议设置阈值为0.8 。值也可能为NULL,表示SDK没有返回相应分数
     */
    private OcrIdcardStatelessScore score;
    /**
     * 身份证来源类型：normal 正常拍摄，photocopy 复印件， ps PS（身份证主体文字部分存在马赛克、水印、涂鸦等）， reversion 屏幕翻拍， other 其他， unknown 未知（识别失败）
     */
    private String type;

}
