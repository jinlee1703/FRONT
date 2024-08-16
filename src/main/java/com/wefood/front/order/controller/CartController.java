package com.wefood.front.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wefood.front.order.dto.CartProductRequest;
import com.wefood.front.order.dto.CartProductResponse;
import com.wefood.front.order.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
        List<CartProductResponse> products;
        if (userId != null) {
            model.addAttribute("login", "Y");
            products = cartService.getCartProductByUserId(userId);
        } else {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                products = objectMapper.readValue(URLDecoder.decode(cart, StandardCharsets.UTF_8), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        model.addAttribute("products", products);
        return "/cart";
    }

    @PostMapping
    public String saveCart(@RequestBody CartProductRequest requestDto) {
        System.out.println("cart");
        cartService.saveCart(requestDto);
        return "redirect:/cart";
    }
}
