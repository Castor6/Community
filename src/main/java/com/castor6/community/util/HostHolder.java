package com.castor6.community.util;

import com.castor6.community.bean.User;
import org.springframework.stereotype.Component;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-27 20:36
 * @Description 承载用户对象的容器，保证了线程隔离，不会有线程安全问题
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
