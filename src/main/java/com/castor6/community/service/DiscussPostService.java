package com.castor6.community.service;

import com.castor6.community.bean.DiscussPost;
import com.github.pagehelper.PageInfo;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-24 22:01
 * @Description
 */
public interface DiscussPostService {
    PageInfo<DiscussPost> pageByUserId(int userId, int pageNum, int pageSize, int navigatePages);
    int getDiscussPostRows(int userId);
}
