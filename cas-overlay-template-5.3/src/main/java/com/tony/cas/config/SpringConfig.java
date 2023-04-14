package com.tony.cas.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 为了让spring 加载 这个包下 标注了  @Service @Component @Controller 等注解的Bean
 */
@Configuration
@ComponentScan("com.tony.cas")
public class SpringConfig {
}
