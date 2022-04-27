package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_cust_user_contact")
public class CustUserContact extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 关系
     */
    private String relation;

    /**
     * 关系代码
     */
    @Column(name = "relation_code")
    private String relationCode;

    /**
     * 第几紧急联系人
     */
    private Integer level;

}