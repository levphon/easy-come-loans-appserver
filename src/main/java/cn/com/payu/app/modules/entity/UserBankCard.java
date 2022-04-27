package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@Table(name = "t_cust_user_bankcard")
public class UserBankCard extends BaseEntity {


    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 银行编号
     */
    @Column(name = "bank_code")
    private String bankCode;

    /**
     * 银行名称
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 账户姓名
     */
    @Column(name = "account_name")
    private String accountName;

    /**
     * 银行卡账号
     */
    @Column(name = "account_no")
    private String accountNo;

    /**
     * 银行预留手机号
     */
    @Column(name = "reserve_mobile")
    private String reserveMobile;

    /**
     * 是否默认卡（0=否 1=是）
     */
    @Column(name = "is_default")
    private Byte isDefault;

}