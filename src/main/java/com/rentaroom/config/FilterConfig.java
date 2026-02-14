package com.rentaroom.config;

import com.rentaroom.filter.ExceptionLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ExceptionLoggingFilter> exceptionLoggingFilter() {
        FilterRegistrationBean<ExceptionLoggingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ExceptionLoggingFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}
