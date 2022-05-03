package cn.com.payu.app.modules.model.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MngCUserExport {

    @ExcelIgnore
    private Long userId;

    @ExcelProperty(value = "可借额度", index = 0)
    private Integer loanAmount;

    @ExcelProperty(value = "借款期限", index = 1)
    private Integer loanPeriod;

    @ExcelProperty(value = "还款方式", index = 2)
    private String loanMethod;

    @ExcelProperty(value = "所属银行", index = 3)
    private String bankName;

    @ExcelProperty(value = "收款账户", index = 4)
    private String bankAccount;

    @ExcelProperty(value = "手机号码", index = 5)
    private String mobile;

    @ExcelProperty(value = "贷款额度", index = 6)
    private String loanLimit;

    @ExcelProperty(value = "贷款时间", index = 7)
    private String loanMonth;

    @ExcelProperty(value = "所在省份", index = 8)
    private String province;

    @ExcelProperty(value = "所在城市", index = 9)
    private String city;

    @ExcelProperty(value = "贷款目的", index = 10)
    private String loanUsage;

    @ExcelProperty(value = "芝麻信用", index = 11)
    private String sesameScore;

    @ExcelProperty(value = "微粒贷", index = 12)
    private String particleLoan;

    @ExcelProperty(value = "京东白条", index = 13)
    private String jdWhiteNote;

    @ExcelProperty(value = "蚂蚁花呗", index = 14)
    private String antSpend;

    @ExcelProperty(value = "教育程度", index = 15)
    private String education;

    @ExcelProperty(value = "职业", index = 16)
    private String occupation;

    @ExcelProperty(value = "工作年限", index = 17)
    private String workMonth;

    @ExcelProperty(value = "社保", index = 18)
    private String socialSecurity;

    @ExcelProperty(value = "公积金", index = 19)
    private String accumulationFund;

    @ExcelProperty(value = "商业保险", index = 20)
    private String commercialInsurance;

    @ExcelProperty(value = "房产", index = 21)
    private String houseAssets;

    @ExcelProperty(value = "车产", index = 22)
    private String vehicleAssets;

    @ExcelProperty(value = "工资发放", index = 23)
    private String salaryPayment;

    @ExcelProperty(value = "信用情况", index = 24)
    private String creditSituation;

    @ExcelProperty(value = "信用卡额度", index = 25)
    private String creditCardAmount;

    @ExcelProperty(value = "真实姓名", index = 26)
    private String realName;

    @ExcelProperty(value = "身份证号", index = 27)
    private String identityNo;

    @ExcelProperty(value = "收入", index = 28)
    private String monthlyIncome;

    @ExcelProperty(value = "注册时间", index = 29)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

}
