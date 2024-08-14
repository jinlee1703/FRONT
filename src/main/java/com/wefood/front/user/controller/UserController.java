package com.wefood.front.user.controller;

import com.wefood.front.user.adaptor.UserAdaptor;
import com.wefood.front.user.dto.request.LoginRequest;
import com.wefood.front.user.dto.request.SignRequest;
import com.wefood.front.user.dto.response.LoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserAdaptor userAdaptor;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, HttpServletResponse response) {

        LoginResponse loginResponse = userAdaptor.login(loginRequest);


        if (Objects.nonNull(loginResponse)) {
            Cookie phoneNumberCookie = new Cookie("phoneNumber", loginRequest.phoneNumber()); // 쿠키 이름과 값 설정
            phoneNumberCookie.setPath("/"); // 쿠키가 적용될 경로 설정
            phoneNumberCookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키의 유효기간 설정 (7일 동안 유효)
            phoneNumberCookie.setHttpOnly(true); // XSS 공격 방지를 위해 HttpOnly 설정
            // 응답에 쿠키 추가
            response.addCookie(phoneNumberCookie);

            Cookie passwordCookie = new Cookie("password", loginRequest.password()); // 쿠키 이름과 값 설정
            passwordCookie.setPath("/"); // 쿠키가 적용될 경로 설정
            passwordCookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키의 유효기간 설정 (7일 동안 유효)
            passwordCookie.setHttpOnly(true); // XSS 공격 방지를 위해 HttpOnly 설정
            // 응답에 쿠키 추가
            response.addCookie(passwordCookie);

            Cookie isSellerCookie;
            if(loginResponse.isSeller()){
                isSellerCookie = new Cookie("isSeller", "true"); // 쿠키 이름과 값 설정
            }else{
                isSellerCookie = new Cookie("isSeller", "false"); // 쿠키 이름과 값 설정
            }

            isSellerCookie.setPath("/"); // 쿠키가 적용될 경로 설정
            isSellerCookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키의 유효기간 설정 (7일 동안 유효)
            isSellerCookie.setHttpOnly(true); // XSS 공격 방지를 위해 HttpOnly 설정
            // 응답에 쿠키 추가
            response.addCookie(isSellerCookie);

            return "redirect:/";
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/sign")
    public String signForm() {
        return "sign";
    }


    @PostMapping("/sign")
    public String sign(@ModelAttribute SignRequest signRequest) {

        userAdaptor.signUp(signRequest);
        return "redirect:/";
    }

}
