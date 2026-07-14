package com.nova.admin.config;

import com.nova.admin.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                // /error 必须放行：登录等接口一旦异常会转发到 /error，
                // 若被拦成 401，会把真实错误（如数据库连接失败）完全盖住。
                .excludePathPatterns("/login", "/error");
    }
}
