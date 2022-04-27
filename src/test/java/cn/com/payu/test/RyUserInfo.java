package cn.com.payu.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RyUserInfo {

    private String id;
    private String username;
    private String icon;
    private String extra;

}
