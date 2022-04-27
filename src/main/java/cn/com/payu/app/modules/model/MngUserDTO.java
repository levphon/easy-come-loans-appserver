package cn.com.payu.app.modules.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MngUserDTO {

    private Long id;

    private String account;

    private String username;

    private Integer deptDepth;

    private String phoneNumber;

    /**
     * 部门id
     */
    private Long departmentId;

    private String departmentName;

    /**
     * 上级id
     */
    private Long superiorId;

    private String superiorName;

    private Integer enableStatus;

    private Long roleId;

    private String roleName;

    private Integer isAdmin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;

    /**
     * 数据类型
     */
    private Integer dataType;

}
