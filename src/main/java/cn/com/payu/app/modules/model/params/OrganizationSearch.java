package cn.com.payu.app.modules.model.params;

import cn.hutool.db.Page;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;

/**
 * @author taoyr
 */
@Data
@Accessors(chain = true)
public class OrganizationSearch extends Page {

    private boolean forPage;

    private String orgName;

    private Integer enableStatus;

    private Long rootId;

    private boolean hasChild;

    private boolean hasUserNumber;

    private Long tenantId;

    private Collection<Long> tenantIds;

    private Collection<Long> supIds;

    private Collection<Long> orgIds;

    private Collection<Long> subIds;

    private Integer biggerDepth;

}
