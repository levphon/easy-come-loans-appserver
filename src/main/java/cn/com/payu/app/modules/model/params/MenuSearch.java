package cn.com.payu.app.modules.model.params;

import cn.hutool.db.Page;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;

@Data
@Accessors(chain = true)
public class MenuSearch extends Page {

    private String name;
    private Long parentId;
    private Integer enableStatus;
    private Collection<Long> roleIds;

}
