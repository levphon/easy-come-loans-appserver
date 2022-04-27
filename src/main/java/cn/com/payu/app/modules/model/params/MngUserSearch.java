package cn.com.payu.app.modules.model.params;

import cn.hutool.db.Page;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author payu
 */
@Data
@Accessors(chain = true)
public class MngUserSearch extends Page {

    private String searchField;

    private Long userId;

    private Integer userStatus;

    private Long departmentId;

    private List<Long> departmentIdList;

}
