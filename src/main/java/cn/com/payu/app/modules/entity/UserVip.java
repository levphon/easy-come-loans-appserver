package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_cust_user_vip")
public class UserVip extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 会员级别
     */
    private Integer level;

    /**
     * 开通状态：0未开通，1开通，2失效，3续费
     */
    private Integer status;

    /**
     * 生效时间
     */
    @Column(name = "effective_date")
    private Date effectiveDate;

    /**
     * 失效时间
     */
    @Column(name = "expiration_date")
    private Date expirationDate;

}