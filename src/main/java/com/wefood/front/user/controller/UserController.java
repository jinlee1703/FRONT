package com.wefood.front.user.controller;

import com.wefood.front.product.dto.ImageResponse;
import com.wefood.front.product.dto.ProductDetailResponse;
import com.wefood.front.product.dto.UploadImageRequestDto;
import com.wefood.front.user.adaptor.UserAdaptor;
import com.wefood.front.user.dto.request.*;
import com.wefood.front.user.dto.response.AddressResponse;
import com.wefood.front.user.dto.response.FarmResponse;
import com.wefood.front.user.dto.response.LoginResponse;
import com.wefood.front.user.dto.response.UserGetResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserAdaptor userAdaptor;

    @GetMapping("/sign-tutorial")
    public String tutorialSignup() {
        return "sign-tutorial";
    }

    @GetMapping("/farm-tutorial")
    public String tutorialFarm() {
        return "farm-tutorial";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // "cart" 쿠키를 찾으면
                if ("id".equals(cookie.getName())) {
                    // 쿠키의 유효 기간을 0으로 설정하여 삭제
                    cookie.setMaxAge(0);
                    // 쿠키의 경로를 지정해야 정상적으로 삭제됨 (쿠키 설정 시의 경로와 일치해야 함)
                    cookie.setPath("/");
                    // 응답에 쿠키 추가
                    response.addCookie(cookie);
                }

                if ("isSeller".equals(cookie.getName())) {
                    // 쿠키의 유효 기간을 0으로 설정하여 삭제
                    cookie.setMaxAge(0);
                    // 쿠키의 경로를 지정해야 정상적으로 삭제됨 (쿠키 설정 시의 경로와 일치해야 함)
                    cookie.setPath("/");
                    // 응답에 쿠키 추가
                    response.addCookie(cookie);
                }


                if ("name".equals(cookie.getName())) {
                    // 쿠키의 유효 기간을 0으로 설정하여 삭제
                    cookie.setMaxAge(0);
                    // 쿠키의 경로를 지정해야 정상적으로 삭제됨 (쿠키 설정 시의 경로와 일치해야 함)
                    cookie.setPath("/");
                    // 응답에 쿠키 추가
                    response.addCookie(cookie);
                }

                if ("password".equals(cookie.getName())) {
                    // 쿠키의 유효 기간을 0으로 설정하여 삭제
                    cookie.setMaxAge(0);
                    // 쿠키의 경로를 지정해야 정상적으로 삭제됨 (쿠키 설정 시의 경로와 일치해야 함)
                    cookie.setPath("/");
                    // 응답에 쿠키 추가
                    response.addCookie(cookie);
                }

                if ("phoneNumber".equals(cookie.getName())) {
                    // 쿠키의 유효 기간을 0으로 설정하여 삭제
                    cookie.setMaxAge(0);
                    // 쿠키의 경로를 지정해야 정상적으로 삭제됨 (쿠키 설정 시의 경로와 일치해야 함)
                    cookie.setPath("/");
                    // 응답에 쿠키 추가
                    response.addCookie(cookie);
                }

            }
        }

        return "redirect:/";
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

            Cookie idCookie = new Cookie("id", String.valueOf(loginResponse.id())); // 쿠키 이름과 값 설정
            passwordCookie.setPath("/"); // 쿠키가 적용될 경로 설정
            passwordCookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키의 유효기간 설정 (7일 동안 유효)
            passwordCookie.setHttpOnly(true); // XSS 공격 방지를 위해 HttpOnly 설정
            // 응답에 쿠키 추가
            response.addCookie(idCookie);

            Cookie nameCookie = new Cookie("name", String.valueOf(loginResponse.name())); // 쿠키 이름과 값 설정
            nameCookie.setPath("/"); // 쿠키가 적용될 경로 설정
            nameCookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키의 유효기간 설정 (7일 동안 유효)
            nameCookie.setHttpOnly(true); // XSS 공격 방지를 위해 HttpOnly 설정
            // 응답에 쿠키 추가
            response.addCookie(nameCookie);

            Cookie isSellerCookie;
            if (loginResponse.isSeller()) {
                isSellerCookie = new Cookie("isSeller", "true"); // 쿠키 이름과 값 설정
            } else {
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

    @GetMapping("/farm")
    public String farmForm(@CookieValue Long id, Model model) {

        FarmResponse farmResponse = userAdaptor.getFarm(id);

        if (farmResponse != null) {
            List<ImageResponse> farmImage = userAdaptor.getFarmImage(farmResponse.getId());
            // sequence대로 정렬
            farmImage.sort(Comparator.comparingInt(ImageResponse::getSequence));
            farmResponse.setDetail(farmResponse.getDetail().replace("\n", "<br>"));
            model.addAttribute("farmImage", farmImage);
        }

        model.addAttribute("farm", farmResponse);

        return "farm";
    }

    @PostMapping("/farm-post")
    public String farmPost(@CookieValue Long id, @ModelAttribute FarmRequest farmRequest) {
        userAdaptor.farmCreate(farmRequest, id);
        return "redirect:/farm";
    }


    @PostMapping("/farm-image")
    public String farmImage(@RequestParam("images") MultipartFile[] files, @RequestParam Long id) throws IOException {
        userAdaptor.farmImageCreate(files, id);
        return "redirect:/";
    }


    @PostMapping("/{id}/address")
    public String address(@ModelAttribute AddressRequest addressRequest, @PathVariable String id) {

        userAdaptor.createAddress(addressRequest, id);
        return "redirect:/mypage";
    }

    @GetMapping("/mypage")
    public String myPage(@CookieValue(name = "password", required = false) String password, @CookieValue(name = "phoneNumber", required = false) String phoneNumber,
                         @CookieValue(name = "id", required = false) String id, Model model) {

        if (Objects.isNull(password)) {
            return "redirect:/login?auth=false";
        }

        UserGetResponse userGetResponse = userAdaptor.findUser(new UserGetRequest(phoneNumber, password));
        AddressResponse addressResponse = userAdaptor.findAddress(userGetResponse.id());
        model.addAttribute("user", userGetResponse);
        model.addAttribute("address", addressResponse);
        model.addAttribute("id", id);
        return "my-page";
    }

}
