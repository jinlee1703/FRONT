package com.wefood.front.config;

import com.wefood.front.global.interceptor.LoginInterceptor;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class WebClientConfig implements WebMvcConfigurer {

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(3L))
                .setReadTimeout(Duration.ofSeconds(3L))
                .build();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**"); // 모든 경로에 대해 인터셉터 적용
    }


}
