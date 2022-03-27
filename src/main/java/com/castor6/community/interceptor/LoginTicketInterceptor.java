package com.castor6.community.interceptor;

import com.castor6.community.bean.LoginTicket;
import com.castor6.community.bean.User;
import com.castor6.community.service.UserService;
import com.castor6.community.util.CookieUtil;
import com.castor6.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-27 20:28
 * @Description 拦截所有请求，检查该请求的发起者有没有登录，若有登录，让本次请求持有该登录的用户信息
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    //请求对应的控制器方法执行前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中获取登陆凭证
        String ticket = CookieUtil.getValue(request, "ticket");
        if(ticket != null){     //有登录
            // 查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户，可通过hostHolder随时随地获取用户对象，例如控制器方法中有这方面的业务
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    //在模板引擎进行页面渲染前执行，将持有的用户对象存入ModelAndView中
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    //请求结束，要响应浏览器前执行，将本次请求的用户对象删掉，避免占用额外的内存
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
