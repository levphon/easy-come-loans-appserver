package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_cust_user_feedback")
public class UserFeedback extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;


    /**
     * 反馈项
     */
    private String feedback;

    /**
     * 用户号码
     */
    private String mobile;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 反馈图片
     */
    private String images;

}