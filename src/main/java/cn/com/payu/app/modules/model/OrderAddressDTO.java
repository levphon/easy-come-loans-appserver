package cn.com.payu.app.modules.model;

import lombok.Data;

@Data
public class OrderAddressDTO {

    private Long orderId;

    private String name;

    private String tel;

    private String address;

    private String expressNo;

}
