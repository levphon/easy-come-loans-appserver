package cn.com.payu.app.modules.model.params;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author taoyr
 */
@Data
@Accessors(chain = true)
public class RoleBO implements Serializable {

    private Long roleId;

    @NotBlank
    @Size(max = 50)
    private String roleName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（1正常 ，2停用）
     */
    @NotNull(message = "启用状态不能为空")
    private Integer enableStatus;

    /**
     * 选中菜单列表
     */
    private List<Long> menuIdList;

}
