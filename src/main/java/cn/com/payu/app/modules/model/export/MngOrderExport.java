package cn.com.payu.app.modules.model.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MngOrderExport {

    @ExcelIgnore
    private Long id;

    @ExcelProperty(value = "订单编号", index = 0)
    private String orderNo;

    @ExcelProperty(value = "订单创建时间", index = 1)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    @ExcelProperty(value = "订单类型", index = 2)
    private String orderType;

    @ExcelProperty(value = "订单支付金额", index = 3)
    private String amount;

    @ExcelProperty(value = "用户姓名", index = 4)
    private String realName;

    @ExcelProperty(value = "用户手机号码", index = 5)
    private String account;

    @ExcelProperty(value = "来源渠道", index = 6)
    private String channel;

    @ExcelProperty(value = "APP", index = 7)
    private String fromApp;

}
