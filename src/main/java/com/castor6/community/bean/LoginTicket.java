package com.castor6.community.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-27 15:41
 * @Description 登陆凭证
 */
@Data
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket;
    private int status;     //0表示有效，1表示失效（可能是过期，也可能是重复登录，至于为什么不删掉，可以用作访问量等统计）
    private Date expired;
}
