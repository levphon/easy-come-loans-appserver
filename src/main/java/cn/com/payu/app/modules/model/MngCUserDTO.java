package cn.com.payu.app.modules.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MngCUserDTO {

    private Long userId;

    private String mobile;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;

    /**
     * 所需额度
     */
    private BigDecimal loanAmount;

    /**
     * 贷款期数
     */
    private Integer loanPeriod;

    /**
     * 还款方式
     */
    private String loanMethod;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String identityNo;

    /**
     * 所属银行
     */
    private String bankName;

    /**
     * 所属银行
     */
    private String bankNameCode;

    /**
     * 银行卡账号
     */
    private String bankAccount;

    /**
     * 所在省
     */
    private String province;

    /**
     * 所在省code
     */
    private String provinceCode;

    /**
     * 所在市
     */
    private String city;

    /**
     * 所在市code
     */
    private String cityCode;

    /**
     * 所在住址
     */
    private String address;

    /**
     * 婚姻状况
     */
    private String maritalStatus;

    /**
     * 婚姻状况
     */
    private String maritalStatusCode;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 教育学历
     */
    private String education;

    /**
     * 教育学历
     */
    private String educationCode;

    /**
     * 贷款额度
     */
    private String loanLimit;

    /**
     * 贷款时间（按月）
     */
    private String loanMonth;

    /**
     * 借款目的
     */
    private String loanUsage;

    /**
     * 借款目的code
     */
    private String loanUsageCode;

    /**
     * 芝麻分数
     */
    private String sesameScore;

    /**
     * 微粒贷
     */
    private String particleLoan;

    /**
     * 京东白条
     */
    private String jdWhiteNote;

    /**
     * 蚂蚁花呗
     */
    private String antSpend;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 职业code
     */
    private String occupationCode;

    /**
     * 工作年限（按月）
     */
    private String workMonth;

    /**
     * 社保（按月）
     */
    private String socialSecurity;

    /**
     * 公积金（按月）
     */
    private String accumulationFund;

    /**
     * 商业保险
     */
    private String commercialInsurance;

    /**
     * 房产
     */
    private String houseAssets;

    /**
     * 房产
     */
    private String vehicleAssets;

    /**
     * 月收入
     */
    private String monthlyIncome;

    /**
     * 月收入code
     */
    private String monthlyIncomeCode;

    /**
     * 工资发放
     */
    private String salaryPayment;

    /**
     * 信用情况
     */
    private String creditSituation;

    /**
     * 信用卡额度
     */
    private String creditCardAmount;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 公司省份
     */
    private String companyProvince;

    /**
     * 公司城市
     */
    private String companyCity;

    /**
     * 公司地址
     */
    private String companyAddress;

    /**
     * 公司行业
     */
    private String companyIndustry;

    /**
     * 公司行业
     */
    private String companyIndustryCode;

    /**
     * 公司电话
     */
    private String companyTel;

    /**
     * 职位
     */
    private String position;

    /**
     * 职位
     */
    private String positionCode;

}
