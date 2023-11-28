package com.example.NewProject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

@Configuration
public class MvcConfig implements WebMvcConfigurer{




    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        String staticPath = Objects.requireNonNull(getClass().getResource("/static")).getPath();
        registry.addResourceHandler("/staticFiles/**")
                .addResourceLocations("file://" + staticPath + "/");
    }

}
