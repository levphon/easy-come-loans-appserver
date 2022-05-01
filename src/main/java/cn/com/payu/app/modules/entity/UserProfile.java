package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "t_cust_user_profile")
public class UserProfile extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 身份证号
     */
    @Column(name = "identity_no")
    private String identityNo;

    /**
     * 所属银行
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 所属银行
     */
    @Column(name = "bank_name_code")
    private String bankNameCode;

    /**
     * 银行卡账号
     */
    @Column(name = "bank_account")
    private String bankAccount;

    /**
     * 所在省
     */
    private String province;

    /**
     * 所在省code
     */
    @Column(name = "province_code")
    private String provinceCode;

    /**
     * 所在市
     */
    private String city;

    /**
     * 所在市code
     */
    @Column(name = "city_code")
    private String cityCode;

    /**
     * 所在住址
     */
    private String address;

    /**
     * 贷款额度
     */
    @Column(name = "loan_limit")
    private String loanLimit;

    /**
     * 贷款时间（按月）
     */
    @Column(name = "loan_month")
    private String loanMonth;

    /**
     * 教育学历
     */
    private String education;

    /**
     * 教育学历
     */
    @Column(name = "education_code")
    private String educationCode;

    /**
     * 借款目的
     */
    @Column(name = "loan_usage")
    private String loanUsage;

    /**
     * 借款目的code
     */
    @Column(name = "loan_usage_code")
    private String loanUsageCode;

    /**
     * 芝麻分数
     */
    @Column(name = "sesame_score")
    private String sesameScore;

    /**
     * 微粒贷
     */
    @Column(name = "particle_loan")
    private String particleLoan;

    /**
     * 京东白条
     */
    @Column(name = "jd_white_note")
    private String jdWhiteNote;

    /**
     * 蚂蚁花呗
     */
    @Column(name = "ant_spend")
    private String antSpend;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 职业code
     */
    @Column(name = "occupation_code")
    private String occupationCode;

    /**
     * 工作年限（按月）
     */
    @Column(name = "work_month")
    private String workMonth;

    /**
     * 社保（按月）
     */
    @Column(name = "social_security")
    private String socialSecurity;

    /**
     * 公积金（按月）
     */
    @Column(name = "accumulation_fund")
    private String accumulationFund;

    /**
     * 商业保险
     */
    @Column(name = "commercial_insurance")
    private String commercialInsurance;

    /**
     * 房产
     */
    @Column(name = "house_assets")
    private String houseAssets;

    /**
     * 房产
     */
    @Column(name = "vehicle_assets")
    private String vehicleAssets;

    /**
     * 月收入
     */
    @Column(name = "monthly_income")
    private String monthlyIncome;

    /**
     * 月收入code
     */
    @Column(name = "monthly_income_code")
    private String monthlyIncomeCode;

    /**
     * 工资发放
     */
    @Column(name = "salary_payment")
    private String salaryPayment;

    /**
     * 信用情况
     */
    @Column(name = "credit_situation")
    private String creditSituation;

    /**
     * 信用卡额度
     */
    @Column(name = "credit_card_amount")
    private String creditCardAmount;

    /**
     * 婚姻状况
     */
    @Column(name = "marital_status")
    private String maritalStatus;

    /**
     * 婚姻状况
     */
    @Column(name = "marital_status_code")
    private String maritalStatusCode;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 公司省份
     */
    @Column(name = "company_province")
    private String companyProvince;

    /**
     * 公司城市
     */
    @Column(name = "company_city")
    private String companyCity;

    /**
     * 公司地址
     */
    @Column(name = "company_address")
    private String companyAddress;

    /**
     * 公司行业
     */
    @Column(name = "company_industry")
    private String companyIndustry;

    /**
     * 公司行业
     */
    @Column(name = "company_industry_code")
    private String companyIndustryCode;

    /**
     * 公司电话
     */
    @Column(name = "company_tel")
    private String companyTel;

    /**
     * 职位
     */
    private String position;

    /**
     * 职位
     */
    @Column(name = "position_code")
    private String positionCode;

    /**
     * 所需额度
     */
    @Column(name = "loan_amount")
    private BigDecimal loanAmount;

    /**
     * 贷款期数
     */
    @Column(name = "loan_period")
    private Integer loanPeriod;

    /**
     * 还款方式
     */
    @Column(name = "loan_method")
    private String loanMethod;

}