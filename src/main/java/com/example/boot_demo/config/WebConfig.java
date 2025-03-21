package com.example.boot_demo.config;

import com.example.boot_demo.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // 登录和注册接口不拦截
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/user/login", "/user/register",
                        "/admin/login", "/admin/register");
    }

}
