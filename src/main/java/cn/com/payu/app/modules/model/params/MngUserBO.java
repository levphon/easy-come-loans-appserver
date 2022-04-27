package cn.com.payu.app.modules.model.params;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author payu
 */
@Data
@Accessors(chain = true)
public class MngUserBO {

    private Long id;

    @Size(min = 5, max = 50)
    @NotBlank
    private String account;

    @Size(min = 2, max = 50)
    @NotBlank
    private String username;

    private String password;

    @NotBlank(message = "手机号码不能为空")
    private String phoneNumber;

    @NotNull(message = "启用状态不能为空")
    private Integer enableStatus;

    private String remark;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 部门id
     */
    @NotNull(message = "所属渠道不能为空")
    private Long departmentId;

    /**
     * 上级id
     */
    private Long superiorId;

    /**
     * 数据类型
     */
    private Integer dataType;

    @NotNull(message = "角色不能为空")
    private Long roleId;

}
