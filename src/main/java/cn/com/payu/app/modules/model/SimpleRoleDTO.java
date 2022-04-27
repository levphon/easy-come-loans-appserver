package cn.com.payu.app.modules.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author payu
 */
@Setter
@Getter
@Accessors(chain = true)
public class SimpleRoleDTO {

    private Long roleId;
    private String roleName;

}
