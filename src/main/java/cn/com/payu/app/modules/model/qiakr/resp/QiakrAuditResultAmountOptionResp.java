package cn.com.payu.app.modules.model.qiakr.resp;

import lombok.Data;

import java.util.List;

@Data
public class QiakrAuditResultAmountOptionResp {

    private Integer min;
    private Integer max;
    private Integer step;
    private List<Integer> periods;

}
