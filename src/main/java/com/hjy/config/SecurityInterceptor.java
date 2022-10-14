package com.hjy.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


    @Component
    public class SecurityInterceptor implements HandlerInterceptor {
        public final static String SESSION_KEY = "user";
        public final static String USERNAME = "uname";


        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            System.out.println("拦截到");
            Cookie[] cookies = request.getCookies();
            String path =request.getServletPath();
            if(path.contains("article") || path.contains("login")){

                return true;
            }
            if (cookies != null)
            {
                for (Cookie cookie : cookies) {
                    System.out.println(cookie.getName() + "拦截器中的cookie");
                    if (cookie.getName().equals(SESSION_KEY)) {

                        return true;
                    }
                }
            }
            response.sendRedirect("/blog/login.html");
            return false;

        }
    }

