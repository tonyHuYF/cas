package com.tony.cas.config;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 密码加密及校验
 */
public class CustomPasswordEncoder implements PasswordEncoder {
    private final Logger logger = LoggerFactory.getLogger(CustomPasswordEncoder.class);

    /**
     * 对密码加密
     */
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            //对数据进行md5加密
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(rawPassword.toString().getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            logger.error("对密码进行md5异常", e);
            return null;
        }
    }

    /**
     * 判断密码是否匹配
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        //判断密码为空，直接返回false
        if (StrUtil.isBlank(rawPassword)) {
            return false;
        }
        //调用上面的encode 对请求密码进行MD5处理
        String pass = this.encode(rawPassword.toString());
        return pass.equals(encodedPassword);
    }
}
