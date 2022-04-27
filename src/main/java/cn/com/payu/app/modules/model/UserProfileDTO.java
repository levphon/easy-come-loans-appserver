package cn.com.payu.app.modules.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserProfileDTO {

    private Long userId;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户名
     */
    private String username;

    private String realName;
    private String identityNo;

    private String maritalStatus;
    private String monthlyIncome;

    private String assets;
    private String education;

    private String bankAccount;
    private String bankName;

    /**
     * 公司名称
     */
    private String company;
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
     * 公司电话
     */
    private String companyTel;
    /**
     * 职位
     */
    private String position;
    /**
     * 职业
     */
    private String occupation;
    /**
     * 负债情况
     */
    private String hasLoans;
    /**
     * 借款用途
     */
    private String loanUsage;

    private Integer loanPeriod;

    private BigDecimal loanAmount;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 注册日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date registerDate;

}
