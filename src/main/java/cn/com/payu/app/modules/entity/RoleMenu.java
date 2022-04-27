package cn.com.payu.app.modules.entity;

import cn.com.payu.app.modules.model.SysUser;
import cn.com.payu.app.modules.utils.MngContextHolder;
import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_role_menu")
@Accessors(chain = true)
public class RoleMenu extends BaseEntity {

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Long roleId;
    /**
     * 菜单id
     */
    @Column(name = "menu_id")
    private Long menuNo;

    public RoleMenu() {
        super();
    }

    public RoleMenu(boolean isAdd) {
        if (isAdd) {
            SysUser user = MngContextHolder.getUser();
            this.setCreatedBy(user.getUserId());
            this.setCreatedDate(new Date());
        }
    }
}