package com.wefood.front.user.controller;

import com.wefood.front.user.adaptor.UserAdaptor;
import com.wefood.front.user.dto.request.SignRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserAdaptor userAdaptor;

    @GetMapping
    public String index() {
        return "index";
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
