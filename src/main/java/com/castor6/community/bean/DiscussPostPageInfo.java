package com.castor6.community.bean;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-24 22:41
 * @Description 用于首页展示的讨论帖的分页类，需要取出结果集中的每个帖子，为其单独查询对应的发表用户
 */
@Data
public class DiscussPostPageInfo {
    private PageInfo<DiscussPost> pageInfo;
    private List<Map<String, Object>> list;     //集合中放与帖子数相同个数的哈希表

    public DiscussPostPageInfo(){

    }

    public DiscussPostPageInfo(PageInfo<DiscussPost> pageInfo) {
        this.pageInfo = pageInfo;
        list = new ArrayList<>();
    }
}
