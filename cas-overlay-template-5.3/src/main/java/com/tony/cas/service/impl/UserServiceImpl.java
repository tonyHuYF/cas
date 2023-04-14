package com.tony.cas.service.impl;

import com.tony.cas.dao.UserDao;
import com.tony.cas.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public Map<String, Object> findUserByNameOrMobile(String userCredential) {
        return userDao.findUserByNameOrPhone(userCredential);
    }
}
