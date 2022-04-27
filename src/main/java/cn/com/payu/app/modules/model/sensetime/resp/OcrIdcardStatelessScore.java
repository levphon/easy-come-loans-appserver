package cn.com.payu.app.modules.model.sensetime.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class OcrIdcardStatelessScore {

    private String name;
    private String gender;
    private String nation;
    private String year;
    private String month;
    private String day;
    private String address;
    private String number;

    private String authority;
    @JSONField(name = "timelimit")
    private String timeLimit;

}
