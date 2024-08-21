package com.wefood.front.global.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 컨트롤러에서 요청 처리 후, View 렌더링 전에 호출되는 메서드
        if (modelAndView != null) {
            Cookie[] cookies = request.getCookies();
            boolean isLoggedIn = false;
            boolean isSeller = false;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("id".equals(cookie.getName())) {
                        isLoggedIn = true;
                    } else if ("isSeller".equals(cookie.getName())) {
                        isSeller = true;
                    }
                }
            }

            modelAndView.addObject("login", isLoggedIn);
            modelAndView.addObject("isSeller", isSeller);
        }
    }

}
