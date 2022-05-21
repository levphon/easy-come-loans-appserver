package cn.com.payu.app.modules.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author taoyr
 */
@Data
@Accessors(chain = true)
public class DepartmentUserCount {

    private Long superiorId;

    private Long departmentId;

    private Long userId;

    private Integer depth;

}
