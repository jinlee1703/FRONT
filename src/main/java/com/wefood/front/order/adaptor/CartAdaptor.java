package com.wefood.front.order.adaptor;

import com.wefood.front.config.BackAdaptorProperties;
import com.wefood.front.global.Message;
import com.wefood.front.order.dto.CartProductRequest;
import com.wefood.front.order.dto.CartResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
public class CartAdaptor {

    private static final String URL = "/api/cart";
    private final RestTemplate restTemplate;
    private final BackAdaptorProperties backAdaptorProperties;

    public CartAdaptor(BackAdaptorProperties backAdaptorProperties, RestTemplate restTemplate) {
        this.backAdaptorProperties = backAdaptorProperties;
        this.restTemplate = restTemplate;
    }

    public Message<List<CartResponse>> getCartProduct(Long userId) {
        URI uri = UriComponentsBuilder
                .fromUriString(backAdaptorProperties.getAddress())
                .path(URL)
                .queryParam("userId", userId)
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Message<List<CartResponse>>> exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });

        return exchange.getBody();
    }

    public void saveCartProduct(CartProductRequest request) {
        URI uri = UriComponentsBuilder.fromUriString(backAdaptorProperties.getAddress()).path(URL).build().toUri();
        restTemplate.postForEntity(uri, request, Void.class);
    }
}
