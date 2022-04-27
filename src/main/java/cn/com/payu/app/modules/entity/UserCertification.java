package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_cust_user_certification")
public class UserCertification extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 自拍照链接
     */
    @Column(name = "self_photo")
    private String selfPhoto;

    /**
     * 自拍视频链接
     */
    @Column(name = "self_video")
    private String selfVideo;

    /**
     * 认证状态：0未（完成）认证，1认证通过，2认证失败
     */
    private Integer status;

    /**
     * 认证日期
     */
    @Column(name = "cert_date")
    private Date certDate;

}