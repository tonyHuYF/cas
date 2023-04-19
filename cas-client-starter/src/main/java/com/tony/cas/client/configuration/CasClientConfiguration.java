package com.tony.cas.client.configuration;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

/**
 * 自动化配置类
 */
@Configuration
@EnableConfigurationProperties(CasClientConfigurationProperties.class)
public class CasClientConfiguration {

    @Resource
    private CasClientConfigurationProperties casClientConfigurationProperties;

    /**
     * 配置登出过滤器
     */
    @Bean
    public FilterRegistrationBean filterSingleRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SingleSignOutFilter());
        //设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerUrlPrefix", casClientConfigurationProperties.getServerUrlPrefix());
        registration.setInitParameters(initParameters);
        //设定加载的顺序
        registration.setOrder(1);
        return registration;
    }

    /**
     * 配置过滤验证器 这里用的是Cas30ProxyReceivingTicketValidationFilter
     */
    @Bean
    public FilterRegistrationBean filterValidationRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
        //设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerUrlPrefix", casClientConfigurationProperties.getServerUrlPrefix());
        initParameters.put("serverName", casClientConfigurationProperties.getClientHostUrl());
        initParameters.put("useSession", "true");
        registration.setInitParameters(initParameters);
        //设定加载的顺序
        registration.setOrder(2);
        return registration;
    }

    /**
     * 配置授权过滤器
     */
    @Bean
    public FilterRegistrationBean filterAuthenticationRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        //设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerLoginUrl", casClientConfigurationProperties.getServerLoginUrl());
        initParameters.put("serverName", casClientConfigurationProperties.getClientHostUrl());

        if (casClientConfigurationProperties.getIgnorePattern() != null &&
                !"".equals(casClientConfigurationProperties.getIgnorePattern())) {
            initParameters.put("ignorePattern", casClientConfigurationProperties.getIgnorePattern());
        }

        //自定义urlPatternMatcherStrategy 验证规则
        if (casClientConfigurationProperties.getIgnoreUrlPatternType() != null &&
                !"".equals(casClientConfigurationProperties.getIgnoreUrlPatternType())) {
            initParameters.put("ignoreUrlPatternType", casClientConfigurationProperties.getIgnoreUrlPatternType());
        }

        registration.setInitParameters(initParameters);
        //设定加载的顺序
        registration.setOrder(3);
        return registration;
    }

    /**
     * request wraper过滤器
     */
    @Bean
    public FilterRegistrationBean filterWrapperRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestWrapperFilter());
        //设定匹配的路径
        registration.addUrlPatterns("/*");
        //设定加载的顺序
        registration.setOrder(4);
        return registration;
    }

    /**
     * 添加监听器
     */
    @Bean
    public ServletListenerRegistrationBean<EventListener> singleSignOutListenerRegistration() {
        ServletListenerRegistrationBean<EventListener> registration = new ServletListenerRegistrationBean<>();
        registration.setListener(new SingleSignOutHttpSessionListener());
        registration.setOrder(1);
        return registration;
    }

}
