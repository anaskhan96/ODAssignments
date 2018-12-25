package com.example.identity;


import com.example.identity.middleware.HomeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registering a middleware for /home, for authentication purposes
        registry.addInterceptor(new HomeInterceptor()).addPathPatterns("/home");
    }
}
