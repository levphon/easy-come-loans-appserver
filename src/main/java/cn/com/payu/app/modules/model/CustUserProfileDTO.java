package cn.com.payu.app.modules.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CustUserProfileDTO {

    private Long cuserId;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户名
     */
    private String username;

    /**
     * 平台账号号
     */
    private String unionId;

    /**
     * 手机号码
     */
    private String account;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    private Integer age;

    /**
     * 星座
     */
    private String starSign;

    /**
     * 身高
     */
    private Float stature;

    /**
     * 体重
     */
    private Float weight;

    /**
     * 职业
     */
    private String profession;

    /**
     * 教育学历
     */
    private Integer education;

    /**
     * 常驻城市
     */
    private String livingCity;

    /**
     * 爱好
     */
    private String hobby;

    /**
     * 喜欢对象
     */
    private String likeObject;

    /**
     * 情感状态
     */
    private Integer emotion;

    /**
     * 注册日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date registerDate;

    private Integer status;

    /**
     * 在线状态：true在线，false离线
     */
    private boolean online;

}
