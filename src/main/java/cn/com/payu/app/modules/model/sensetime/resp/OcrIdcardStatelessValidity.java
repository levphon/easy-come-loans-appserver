package cn.com.payu.app.modules.model.sensetime.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class OcrIdcardStatelessValidity {

    private String name;
    private String gender;
    private String birthday;
    private String address;
    private String number;

    private String authority;
    @JSONField(name = "timelimit")
    private String timeLimit;

}
