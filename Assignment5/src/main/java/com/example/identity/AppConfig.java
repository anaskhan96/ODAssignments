package com.example.identity;


import com.example.identity.middleware.HomeInterceptor;
import com.example.identity.middleware.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registering a middlewares
        registry.addInterceptor(new HomeInterceptor()).addPathPatterns("/home");
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/login/generateOTP");
    }
}
