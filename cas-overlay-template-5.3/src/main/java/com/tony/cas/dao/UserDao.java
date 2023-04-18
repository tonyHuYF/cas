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
            return jdbcTemplate.queryForMap("SELECT `system_user`.*,`system_account`.`password` FROM `system_account` LEFT JOIN `system_user` ON `system_account`.user_id = `system_user`.user_id WHERE `system_account`.account_name=? and `system_user`.status = 0", userCredential);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
