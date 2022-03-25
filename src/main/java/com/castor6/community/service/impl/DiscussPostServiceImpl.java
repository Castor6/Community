package com.castor6.community.service.impl;

import com.castor6.community.service.DiscussPostService;
import com.castor6.community.bean.DiscussPost;
import com.castor6.community.mapper.DiscussPostMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-24 22:12
 * @Description
 */
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Override
    public PageInfo<DiscussPost> pageByUserId(int userId, int pageNum, int pageSize, int navigatePages) {
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<DiscussPost> page = new PageInfo<>(discussPostMapper.selectDiscussPosts(userId), navigatePages);
        return page;
    }

    @Override
    public int getDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
