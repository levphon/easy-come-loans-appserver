package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "t_cust_user_withdraw_apply")
public class UserWithdrawApply extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 对应订单id
     */
    @Column(name = "order_id")
    private Long orderId;

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
     * 金额，单位元
     */
    private BigDecimal amount;

    /**
     * 请求状态：0申请，1通过，2拒绝
     */
    private Integer status;

    /**
     * 基于mysql乐观锁，解决并发访问
     */
    @tk.mybatis.mapper.annotation.Version
    private Integer version;

}