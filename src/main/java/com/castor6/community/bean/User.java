package com.castor6.community.bean;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-24 20:54
 * @Description     用户实体类
 */
@Data
public class User {
    private int id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String salt;    //为密码拼接上一个随机字符串（盐）提高密码的安全性

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    private int type;   //0表示普通用户
    private int status;     //0表示未激活
    private String activationCode;  //激活码
    private String headerUrl;   //头像地址
    private Date createTime;    //创建时间
}
