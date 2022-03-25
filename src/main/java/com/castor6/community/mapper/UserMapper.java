package com.castor6.community.mapper;

import com.castor6.community.bean.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-24 21:03
 * @Description
 */
@Mapper
public interface UserMapper {
    User selectUserById(int id);
    User selectUserByUserName(String username);
    User selectUserByEmail(String email);
    int insertUser(User user);
    int updateUserStatus(int id, int status);
    int updateUserHeader(int id, String headerUrl);
    int updateUserPassword(int id, String password);
}
