package com.tony.cas.client.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AuthConfig {

    @Value("${cas.server-login-url}")
    private String CAS_SERVER_LOGIN_URL;

    @Value("${cas.client-host-url}")
    private String SERVER_NAME;

    @Value("${cas.ignore-pattern}")
    private String IGNORE_PATTERN;

    /**
     * 授权过滤器
     */
    @Bean
    public FilterRegistrationBean filterAuthenticationRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        //设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerLoginUrl", CAS_SERVER_LOGIN_URL);
        initParameters.put("serverName", SERVER_NAME);
        initParameters.put("ignorePattern", IGNORE_PATTERN);
        registration.setInitParameters(initParameters);
        //设定加载的顺序
        registration.setOrder(1);
        return registration;
    }
}
