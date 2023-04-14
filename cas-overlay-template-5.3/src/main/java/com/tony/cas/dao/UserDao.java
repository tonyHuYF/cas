package com.tony.cas.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户账号dao
 */
@Repository("userDao")
public class UserDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过用户账户查询用户信息
     */
    public Map<String, Object> findUserByNameOrPhone(String userCredential) {
        try {
            return jdbcTemplate.queryForMap("SELECT user_info.* FROM user_info WHERE user_info.username=?", userCredential);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
