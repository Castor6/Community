package com.castor6.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-27 23:29
 * @Description 自定义注解标注在需要拦截判断发起请求的浏览器有没有登录状态的方法上
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
