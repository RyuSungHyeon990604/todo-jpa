package com.example.todojpa.config;

import com.example.todojpa.security.LoginFilter;
import com.example.todojpa.security.ReissueFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> loginFilterRegistrationBean(LoginFilter tokenFilter) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(tokenFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> ReissueFilterRegistrationBean(ReissueFilter reissueFilter) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(reissueFilter);
        filterRegistrationBean.addUrlPatterns("/auth/reissue");
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }
}
