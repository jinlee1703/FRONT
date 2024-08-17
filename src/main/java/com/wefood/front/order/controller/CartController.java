package com.wefood.front.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wefood.front.order.dto.CartProductRequest;
import com.wefood.front.order.dto.CartResponse;
import com.wefood.front.order.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String getCart(Model model, @CookieValue(name = "id", required = false) Long userId, @CookieValue(name = "cart", required = false) String cart) {
        List<CartResponse> farms;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                farms = objectMapper.readValue(cart, new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        model.addAttribute("farms", farms);
        return "/cart";
    }

    @PostMapping
    public String saveCart(@RequestBody CartProductRequest requestDto) {
        cartService.saveCart(requestDto);
        return "redirect:/cart";
    }
}
