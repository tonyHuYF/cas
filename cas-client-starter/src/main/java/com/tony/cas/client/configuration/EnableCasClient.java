package com.tony.cas.client.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 配置该注解开启cas功能
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(CasClientConfiguration.class)
public @interface EnableCasClient {
}
