package com.tony.cas.service;

import java.util.Map;

public interface UserService {
     Map<String, Object> findUserByNameOrMobile(String userCredential);
}
