package cn.com.payu.app.modules.model.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;

/**
 * @author: taoyr
 **/
@Data
@Accessors(chain = true)
public class MenuTreeSearch {

    private Collection<Long> roleIds;

    private Collection<Long> menuIds;

    private Collection<Long> menuNos;

    private Collection<Integer> menuTypes;

}
