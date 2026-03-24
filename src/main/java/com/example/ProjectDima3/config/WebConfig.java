package com.example.ProjectDima3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry; // Добавь этот импорт
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Это свяжет красивые URL с твоими файлами в папке static
        registry.addViewController("/register").setViewName("forward:/register.html");
        registry.addViewController("/login").setViewName("forward:/login.html");
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/assets").setViewName("forward:/assets.html");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5500", "http://127.0.0.1:5500")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}