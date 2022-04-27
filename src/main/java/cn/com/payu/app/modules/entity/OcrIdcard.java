package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_ocr_idcard")
public class OcrIdcard extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 正面
     */
    @Column(name = "front_url")
    private String frontUrl;

    /**
     * 反面
     */
    @Column(name = "back_url")
    private String backUrl;

    /**
     * 身份证
     */
    private String cid;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 地址
     */
    private String address;

    /**
     * 民族
     */
    private String nation;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 签发机关
     */
    @Column(name = "issued_by")
    private String issuedBy;

    /**
     * 有效期
     */
    @Column(name = "valid_date")
    private String validDate;

}