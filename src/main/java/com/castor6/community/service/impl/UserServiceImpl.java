package com.castor6.community.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.castor6.community.bean.User;
import com.castor6.community.mapper.UserMapper;
import com.castor6.community.service.UserService;
import com.castor6.community.util.CommunityConstant;
import com.castor6.community.util.CommunityUtil;
import com.castor6.community.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-24 22:26
 * @Description
 */
@Service
public class UserServiceImpl implements UserService, CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public User findUserById(int id) {
        return userMapper.selectUserById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 验证账号
        User u = userMapper.selectUserByUserName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }

        // 验证邮箱
        u = userMapper.selectUserByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }

        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int activation(int userId, String code) {
        User user = userMapper.selectUserById(userId);
        if (user.getStatus() == 1) {    //重复激活
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {     //激活成功
            userMapper.updateUserStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {    //激活失败
            return ACTIVATION_FAILURE;
        }
    }

}
