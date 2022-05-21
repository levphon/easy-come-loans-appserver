package cn.com.payu.app.modules.entity;

import cn.com.payu.app.modules.model.SysUser;
import cn.com.payu.app.modules.utils.MngContextHolder;
import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Accessors(chain = true)
@Data
@Table(name = "t_organization")
public class Organization extends BaseEntity {

    public Organization() {
        super();
    }

    public Organization(boolean isAdd) {
        if (isAdd) {
            SysUser user = MngContextHolder.getUser();
            this.setCreatedBy(user.getUserId());
            this.setCreatedDate(new Date());
        }
    }

    /**
     * 上级id
     */
    @Column(name = "superior_id")
    private Long superiorId;

    /**
     * 下级id
     */
    @Column(name = "sub_id")
    private Long subId;

    /**
     * 深度(下级节点-上级节点层数)
     */
    private Integer depth;

    /**
     * 租户id
     */
    @Column(name = "tenant_id")
    private Long tenantId;

}