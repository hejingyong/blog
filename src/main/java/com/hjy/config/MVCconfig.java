package com.hjy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
@Configuration

public class MVCconfig implements  WebMvcConfigurer{


    @Autowired
    private SecurityInterceptor mySecurityInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("admin/login").setViewName("admin/login");
        registry.addViewController("login.html").setViewName("admin/login");

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {




        registry.addInterceptor(mySecurityInterceptor)
               .addPathPatterns("/**")
                .excludePathPatterns("//upload/**","/css/**","/bootstrap/**",
                        "/editormd/**","/js/**","/admin/forgotPwd","/admin/sendMail",
                        "/admin/register","admin/changePwd"

                        )
                ;


    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:C:/Users/hjy/IdeaProjects/blog/src/main/resources/static/editormd/work/public/upload");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
