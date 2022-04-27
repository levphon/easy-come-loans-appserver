package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_sensetime_liveness")
public class SensetimeLiveness extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "cuser_id")
    private Long cuserId;

    /**
     * 请求序列号
     */
    @Column(name = "biz_no")
    private String bizNo;

    /**
     * token
     */
    @Column(name = "biz_token")
    private String bizToken;

    /**
     * 活体结果原始帧图
     */
    @Column(name = "liveness_image")
    private String livenessImage;

    /**
     * 活体结果人脸裁剪图
     */
    @Column(name = "liveness_face_image")
    private String livenessFaceImage;

    /**
     * 状态：0默认，1通过，2不通过
     */
    private Integer status;

}