package com.wefood.front.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wefood.front.product.adaptor.MarketPriceAdaptor;
import com.wefood.front.product.dto.MarketPriceResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarketPriceService {

    private final MarketPriceAdaptor marketPriceAdaptor;


    public MarketPriceService(MarketPriceAdaptor marketPriceAdaptor) {
        this.marketPriceAdaptor = marketPriceAdaptor;
    }


    public String getMarketPrice() {
        List<String> list = new ArrayList<>();
        list.add(marketPriceAdaptor.getMarketPrice100());
        list.add(marketPriceAdaptor.getMarketPrice200());
        list.add(marketPriceAdaptor.getMarketPrice300());
        list.add(marketPriceAdaptor.getMarketPrice400());
        return "[" + String.join(",", list) + "]";
    }

    public List<MarketPriceResponse> readValue(String target) {
        List<MarketPriceResponse> response;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(target, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return response;
    }

    public String writeValue(List<MarketPriceResponse> data) {
        String jsonData;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonData = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonData;
    }

    public void setCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, URLEncoder.encode(cookieValue, StandardCharsets.UTF_8));
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }
}
