package com.wefood.front.product.adaptor;

import com.wefood.front.config.MarketPriceProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@Component
public class MarketPriceAdaptor {

    private final MarketPriceProperties marketPriceProperties;
    private final RestTemplate restTemplate;

    public MarketPriceAdaptor(MarketPriceProperties marketPriceProperties, RestTemplate restTemplate) {
        this.marketPriceProperties = marketPriceProperties;
        this.restTemplate = restTemplate;
    }

    public String getMarketPriceGreens() {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(marketPriceProperties.getUrl())
                .queryParam("action", "dailyPriceByCategoryList")
                .queryParam("p_item_category_code", "200")
                .queryParam("p_regday", LocalDate.now().minusDays(1))
                .queryParam("p_convert_kg_yn", "N")
                .queryParam("p_returntype", "json")
                .queryParam("p_cert_key", marketPriceProperties.getKey())
                .queryParam("p_cert_id", marketPriceProperties.getId())
                .build().toUri();

        ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);
        return forEntity.getBody();
    }
    public String getMarketPriceFruits() {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(marketPriceProperties.getUrl())
                .queryParam("action", "dailyPriceByCategoryList")
                .queryParam("p_item_category_code", "400")
                .queryParam("p_regday", LocalDate.now().minusDays(1))
                .queryParam("p_convert_kg_yn", "N")
                .queryParam("p_returntype", "json")
                .queryParam("p_cert_key", marketPriceProperties.getKey())
                .queryParam("p_cert_id", marketPriceProperties.getId())
                .build().toUri();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);
        return forEntity.getBody();
    }
    public String getMarketPriceLiveStocks() {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(marketPriceProperties.getUrl())
                .queryParam("action", "dailyPriceByCategoryList")
                .queryParam("p_item_category_code", "500")
                .queryParam("p_regday", LocalDate.now().minusDays(1))
                .queryParam("p_convert_kg_yn", "N")
                .queryParam("p_returntype", "json")
                .queryParam("p_cert_key", marketPriceProperties.getKey())
                .queryParam("p_cert_id", marketPriceProperties.getId())
                .build().toUri();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);
        return forEntity.getBody();
    }
    public String getMarketPriceFoodCrops() {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(marketPriceProperties.getUrl())
                .queryParam("action", "dailyPriceByCategoryList")
                .queryParam("p_item_category_code", "100")
                .queryParam("p_regday", LocalDate.now().minusDays(1))
                .queryParam("p_convert_kg_yn", "N")
                .queryParam("p_returntype", "json")
                .queryParam("p_cert_key", marketPriceProperties.getKey())
                .queryParam("p_cert_id", marketPriceProperties.getId())
                .build().toUri();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);
        return forEntity.getBody();
    }
}
