package com.wefood.front.product.adaptor;

import com.wefood.front.config.MarketPriceProperties;
import com.wefood.front.product.dto.MarketPriceResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.ParameterizedType;
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

    public String getMarketPrice100() {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(marketPriceProperties.getUrl())
                .queryParam("action", "dailyPriceByCategoryList")
                .queryParam("p_item_category_code", "100")
                .queryParam("p_regday", LocalDate.now().minusDays(2))
                .queryParam("p_convert_kg_yn", "N")
                .queryParam("p_returntype", "json")
                .queryParam("p_cert_key", marketPriceProperties.getKey())
                .queryParam("p_cert_id", marketPriceProperties.getId())
                .build().toUri();

        ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);
        return forEntity.getBody();
    }
    public String getMarketPrice200() {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(marketPriceProperties.getUrl())
                .queryParam("action", "dailyPriceByCategoryList")
                .queryParam("p_item_category_code", "200")
                .queryParam("p_regday", LocalDate.now().minusDays(2))
                .queryParam("p_convert_kg_yn", "N")
                .queryParam("p_returntype", "json")
                .queryParam("p_cert_key", marketPriceProperties.getKey())
                .queryParam("p_cert_id", marketPriceProperties.getId())
                .build().toUri();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);
        return forEntity.getBody();
    }
    public String getMarketPrice300() {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(marketPriceProperties.getUrl())
                .queryParam("action", "dailyPriceByCategoryList")
                .queryParam("p_item_category_code", "300")
                .queryParam("p_regday", LocalDate.now().minusDays(2))
                .queryParam("p_convert_kg_yn", "N")
                .queryParam("p_returntype", "json")
                .queryParam("p_cert_key", marketPriceProperties.getKey())
                .queryParam("p_cert_id", marketPriceProperties.getId())
                .build().toUri();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);
        return forEntity.getBody();
    }
    public String getMarketPrice400() {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(marketPriceProperties.getUrl())
                .queryParam("action", "dailyPriceByCategoryList")
                .queryParam("p_item_category_code", "400")
                .queryParam("p_regday", LocalDate.now().minusDays(2))
                .queryParam("p_convert_kg_yn", "N")
                .queryParam("p_returntype", "json")
                .queryParam("p_cert_key", marketPriceProperties.getKey())
                .queryParam("p_cert_id", marketPriceProperties.getId())
                .build().toUri();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);
        return forEntity.getBody();
    }
}
