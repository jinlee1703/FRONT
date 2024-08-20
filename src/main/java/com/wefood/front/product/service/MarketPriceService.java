package com.wefood.front.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wefood.front.product.adaptor.MarketPriceAdaptor;
import com.wefood.front.product.dto.MarketPriceItemResponse;
import com.wefood.front.product.dto.MarketPriceResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class MarketPriceService {

    private final MarketPriceAdaptor marketPriceAdaptor;


    public MarketPriceService(MarketPriceAdaptor marketPriceAdaptor) {
        this.marketPriceAdaptor = marketPriceAdaptor;
    }


    public List<MarketPriceResponse> saveMarketPrice(HttpServletResponse response, String cookieName) {
        List<String> prices = new ArrayList<>();
        prices.add(marketPriceAdaptor.getMarketPriceGreens());
        prices.add(marketPriceAdaptor.getMarketPriceFruits());
        prices.add(marketPriceAdaptor.getMarketPriceLiveStocks());
        prices.add(marketPriceAdaptor.getMarketPriceFoodCrops());

        List<MarketPriceResponse> marketPriceResponses = new ArrayList<>();
        for (int i = 0; i < prices.size(); i++) {
            MarketPriceResponse marketPrice = readValue(prices.get(i));
            marketPriceResponses.add(marketPrice);
            String compressed = compress(writeValue(marketPrice));
            setCookie(response, cookieName + i, compressed);
        }
        return marketPriceResponses;
    }

    public MarketPriceResponse readValue(String target) {
        MarketPriceResponse response;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(target, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return response;
    }

    public String writeValue(MarketPriceResponse data) {
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
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }

    public String compress(String data) {
        byte[] input = data.getBytes(StandardCharsets.UTF_8);
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();
        byte[] buffer = new byte[9000];
        int compressedDataLength = deflater.deflate(buffer);
        deflater.end();

        byte[] output = new byte[compressedDataLength];
        System.arraycopy(buffer, 0, output, 0, compressedDataLength);
        return Base64.getEncoder().encodeToString(output);
    }

    public String decompress(String compressedData) {
        byte[] input = Base64.getDecoder().decode(compressedData);
        Inflater inflater = new Inflater();
        inflater.setInput(input);

        try {
            byte[] buffer = new byte[9000];
            int resultLength = inflater.inflate(buffer);
            inflater.end();
            return new String(buffer, 0, resultLength, StandardCharsets.UTF_8);
        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MarketPriceItemResponse> getMarketPriceCookie(Long itemId, Cookie[] cookies) {

        MarketPriceResponse response;
        // 200, 400, 500, 100
        switch (String.valueOf(itemId).charAt(0)) {
            case '1' -> response = readValue(getCookieByName("price3", cookies));
            case '2' -> response = readValue(getCookieByName("price0", cookies));
            case '4' -> response = readValue(getCookieByName("price1", cookies));
            case '5' -> response = readValue(getCookieByName("price2", cookies));
            default -> {
                return null;
            }
        }
        return response.getData().getItem().stream().filter(item -> item.getItem_code().equals(String.valueOf(itemId))).toList();
    }

    private String getCookieByName(String name, Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
