package com.castor6.community.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-25 23:33
 * @Description 常用方法的工具类，因为不需要注入容器中的组件，所以直接使用静态方法就可以了
 */
public class CommunityUtil {

    // 生成随机字符串（上传头像，需要为该文件随机取名）
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");    //不需要-
    }

    // MD5加密（不能被解密，但太过简单有规律的密码也容易直接被暴力破解）
    // hello -> abc123def456
    // hello + 3e4a8 -> abc123def456abc （简单密码容易直接被暴力破解，因此为每个用户的密码后面加上一个随机的字符串（盐），提高用户密码的安全性）
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}