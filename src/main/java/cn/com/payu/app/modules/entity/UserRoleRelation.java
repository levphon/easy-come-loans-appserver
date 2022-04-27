package cn.com.payu.app.modules.entity;

import cn.com.payu.app.modules.model.SysUser;
import cn.com.payu.app.modules.utils.MngContextHolder;
import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "t_user_role_relation")
public class UserRoleRelation extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;
    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Long roleId;

    public UserRoleRelation() {
        super();
    }

    public UserRoleRelation(boolean isAdd) {
        if (isAdd) {
            SysUser user = MngContextHolder.getUser();
            this.setCreatedBy(user.getUserId());
            this.setCreatedDate(new Date());
        }
    }

}