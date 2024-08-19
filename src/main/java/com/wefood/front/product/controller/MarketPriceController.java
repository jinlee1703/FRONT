package com.wefood.front.product.controller;

import com.wefood.front.product.dto.MarketPriceResponse;
import com.wefood.front.product.service.MarketPriceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/market-price")
public class MarketPriceController {

    private final MarketPriceService marketPriceService;
    private static final String cookieName = "price";

    public MarketPriceController(MarketPriceService marketPriceService) {
        this.marketPriceService = marketPriceService;
    }

    @GetMapping
    public String getMarketPrice(Model model, @CookieValue(name = cookieName, required = false) String price, HttpServletResponse response) {

        List<MarketPriceResponse> marketPrice;
        if (price == null) {
            System.out.println("cookie x");
            marketPrice = marketPriceService.readValue(marketPriceService.getMarketPrice());
//            marketPriceService.setCookie(response, cookieName, marketPriceService.writeValue(marketPrice));
        } else {
            System.out.println("cookie o");
            marketPrice = marketPriceService.readValue(URLDecoder.decode(price, StandardCharsets.UTF_8));
        }

        model.addAttribute("items1", marketPrice.get(0));
        model.addAttribute("items2", marketPrice.get(1));
        model.addAttribute("items3", marketPrice.get(2));
        model.addAttribute("items4", marketPrice.get(3));

        return "/market-price";
    }
}
