package cn.com.payu.app.modules.model.params;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: taoyr
 **/
@Data
@Accessors(chain = true)
public class OrganizationBO {

    private Long id;

    private String departmentName;

    private Long superiorId;

    private Integer orderNum;

    private Integer enableStatus;

}
