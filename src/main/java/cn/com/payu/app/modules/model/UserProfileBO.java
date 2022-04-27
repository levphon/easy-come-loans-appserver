package cn.com.payu.app.modules.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserProfileBO implements Serializable {

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
    private String bankNameCode;
    /**
     * 银行卡账号
     */
    private String bankAccount;
    /**
     * 婚姻状况
     */
    private String maritalStatus;
    private String maritalStatusCode;
    /**
     * 月收入
     */
    private String monthlyIncome;
    private String monthlyIncomeCode;

    /**
     * 紧急联系人
     */
    private String firstName;
    private String firstPhone;
    private String firstRelation;
    private String firstRelationCode;
    private String secondName;
    private String secondPhone;
    private String secondRelation;
    private String secondRelationCode;

    /**
     * 资产情况
     */
    private String assets;
    /**
     * 教育学历
     */
    private String education;
    private String educationCode;

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
    private String companyIndustryCode;
    /**
     * 公司电话
     */
    private String companyTel;
    /**
     * 职位
     */
    private String position;
    private String positionCode;
    /**
     * 职业
     */
    private String occupation;
    private String occupationCode;
    /**
     * 负债情况
     */
    private String hasLoans;
    /**
     * 借款用途
     */
    private String loanUsage;
    private String loanUsageCode;
    /**
     * 邮箱
     */
    private String email;

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
     * 身份证正面
     */
    private String idCardFront;

    /**
     * 身份证反面
     */
    private String idCardBack;

    /**
     * 民族
     */
    private String nation;

    /**
     * 身份证地址
     */
    private String idCardAddress;

    /**
     * 身份证签发机关
     */
    private String authority;

    /**
     * 身份证有效期
     */
    private String timeLimit;

}
