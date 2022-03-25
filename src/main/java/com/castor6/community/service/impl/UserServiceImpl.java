package com.castor6.community.service.impl;

import com.castor6.community.bean.User;
import com.castor6.community.mapper.UserMapper;
import com.castor6.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-24 22:26
 * @Description
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(int id) {
        return userMapper.selectUserById(id);
    }
}
