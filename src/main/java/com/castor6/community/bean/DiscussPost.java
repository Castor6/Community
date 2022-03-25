package com.castor6.community.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-24 20:29
 * @Description   讨论帖的实体类
 */
@Data
public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;   //0是普通帖；1是置顶帖
    private int status;     //1是精华；2是拉黑帖，不能展示
    private Date createTime;
    private int commentCount;
    private double score;
}
