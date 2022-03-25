package com.castor6.community.service;

import com.castor6.community.bean.User;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-24 22:25
 * @Description
 */
public interface UserService {
    User findUserById(int id);
}
