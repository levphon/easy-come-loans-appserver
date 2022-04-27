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

    @Column(name = "bank_name_code")
    private String bankNameCode;

    /**
     * 银行卡账号
     */
    @Column(name = "bank_account")
    private String bankAccount;
    /**
     * 婚姻状况
     */
    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "marital_status_code")
    private String maritalStatusCode;

    /**
     * 月收入
     */
    @Column(name = "monthly_income")
    private String monthlyIncome;

    @Column(name = "monthly_income_code")
    private String monthlyIncomeCode;

    /**
     * 资产情况
     */
    private String assets;
    /**
     * 教育学历
     */
    private String education;

    @Column(name = "education_code")
    private String educationCode;

    /**
     * 所在省
     */
    private String province;
    /**
     * 所在市
     */
    private String city;
    /**
     * 所在住址
     */
    private String address;

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

    @Column(name = "position_code")
    private String positionCode;

    /**
     * 职业
     */
    private String occupation;

    @Column(name = "occupation_code")
    private String occupationCode;

    /**
     * 负债情况
     */
    @Column(name = "has_loans")
    private String hasLoans;
    /**
     * 借款用途
     */
    @Column(name = "loan_usage")
    private String loanUsage;

    @Column(name = "loan_usage_code")
    private String loanUsageCode;

    @Column(name = "loan_amount")
    private BigDecimal loanAmount;

    @Column(name = "loan_period")
    private Integer loanPeriod;

    /**
     * 邮箱
     */
    private String email;

}