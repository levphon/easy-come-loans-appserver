package cn.com.payu.app.modules.model.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CustUserRegisterExport {

    @ExcelIgnore
    private Long userId;

    @ExcelProperty(value = "用户姓名", index = 0)
    private String realName;

    @ExcelProperty(value = "身份证号", index = 1)
    private String identityNo;

    @ExcelProperty(value = "手机号码", index = 2)
    private String mobile;

    @ExcelProperty(value = "注册时间", index = 3)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerDate;

    @ExcelProperty(value = "所属银行", index = 4)
    private String bankName;

    @ExcelProperty(value = "银行卡号", index = 5)
    private String bankAccount;

    @ExcelProperty(value = "所需额度", index = 6)
    private BigDecimal loanAmount;

    @ExcelProperty(value = "贷款期数", index = 7)
    private Integer loanPeriod;

    @ExcelIgnore
    private Integer vipStatus;

    @ExcelProperty(value = "会员类型", index = 8)
    private String vipStatusStr;

    @ExcelIgnore
    private Integer vipDays;

    @ExcelIgnore
    private Integer vipExpireDays;

    @ExcelIgnore
    private Integer delFlag;

}
