package com.wefood.front.farm.adaptor;

import com.wefood.front.config.BackAdaptorProperties;
import com.wefood.front.farm.dto.FarmListResponse;
import com.wefood.front.global.Message;
import com.wefood.front.global.PageRequest;
import com.wefood.front.product.dto.ProductResponse;
import com.wefood.front.user.dto.response.FarmResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class FarmAdaptor {

    private final BackAdaptorProperties properties;
    private final RestTemplate restTemplate;

    public FarmAdaptor(BackAdaptorProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    public Message<PageRequest<FarmListResponse>> getFarms(Long page, Long size) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.getAddress())
                .path("/api/farm/list")
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Message<PageRequest<FarmListResponse>>> exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });
        return exchange.getBody();
    }

    public Message<FarmResponse> getFarm(Long id) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.getAddress())
                .path("/api/farm/{id}")
                .buildAndExpand(id)
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Message<FarmResponse>> exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });

        return exchange.getBody();
    }

    public Message<PageRequest<ProductResponse>> getProductsByFarm(Long id, Long page, Long size) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.getAddress())
                .path("/api/farm/{id}/product")
                .queryParam("page", page)
                .queryParam("size", size)
                .buildAndExpand(id)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Message<PageRequest<ProductResponse>>> exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });

        return exchange.getBody();
    }
}
