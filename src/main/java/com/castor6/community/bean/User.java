package com.castor6.community.bean;

import lombok.Data;

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
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;  //激活码
    private String headerUrl;   //头像地址
    private Date createTime;    //创建时间
}
