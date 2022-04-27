package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class IndividualPage {

    private UserProfileDTO user;

    /**
     * 会员状态：0非会员，负数失效会员，正数有效会员，数值为会员等级
     */
    private Integer vip;

}
