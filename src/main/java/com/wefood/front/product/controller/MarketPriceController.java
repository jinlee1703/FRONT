package com.wefood.front.product.controller;

import com.wefood.front.product.dto.MarketPriceResponse;
import com.wefood.front.product.service.MarketPriceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/market-price")
public class MarketPriceController {

    private final MarketPriceService marketPriceService;

    public MarketPriceController(MarketPriceService marketPriceService) {
        this.marketPriceService = marketPriceService;
    }

    @GetMapping
    public String getMarketPrice(Model model, @CookieValue(name = "price0", required = false) String price0, @CookieValue(name = "price1", required = false) String price1, @CookieValue(name = "price2", required = false) String price2, @CookieValue(name = "price3", required = false) String price3, HttpServletResponse response) {
        if (price1 == null) {
            List<MarketPriceResponse> prices = marketPriceService.saveMarketPrice(response, "price");
//            String cookieName = "price";
            for (int i = 0; i < prices.size(); i++) {
//                marketPrice = marketPriceService.readValue(prices.get(i));
//                String compressed = marketPriceService.compress(marketPriceService.writeValue(marketPrice));
//                marketPriceService.setCookie(response, cookieName + i, compressed);
                model.addAttribute("items" + i, prices.get(i));
            }
        } else {
            model.addAttribute("items0", marketPriceService.readValue(marketPriceService.decompress(price0)));
            model.addAttribute("items1", marketPriceService.readValue(marketPriceService.decompress(price1)));
            model.addAttribute("items2", marketPriceService.readValue(marketPriceService.decompress(price2)));
            model.addAttribute("items3", marketPriceService.readValue(marketPriceService.decompress(price3)));
        }

        return "market-price";
    }
}
