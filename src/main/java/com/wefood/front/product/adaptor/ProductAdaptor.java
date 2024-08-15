package com.wefood.front.product.adaptor;

import com.wefood.front.config.BackAdaptorProperties;
import com.wefood.front.global.Message;
import com.wefood.front.global.PageRequest;
import com.wefood.front.product.dto.ProductDetailResponse;
import com.wefood.front.product.dto.ProductResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@Component
public class ProductAdaptor {

    private final RestTemplate restTemplate;
    private final BackAdaptorProperties backAdaptorProperties;
    private static final String productURL = "/api/product";

    public ProductAdaptor(RestTemplate restTemplate, BackAdaptorProperties backAdaptorProperties) {
        this.restTemplate = restTemplate;
        this.backAdaptorProperties = backAdaptorProperties;
    }

    public Message<ProductDetailResponse> getProductDetail(Long productId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = UriComponentsBuilder.fromUriString(backAdaptorProperties.getAddress())
                .path(productURL)
                .path("/{productId}")
                .buildAndExpand(productId)
                .toUri();
        ResponseEntity<Message<ProductDetailResponse>> exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });

        return exchange.getBody();
    }

    public Message<List<ProductResponse>> getProducts() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = UriComponentsBuilder.fromUriString(backAdaptorProperties.getAddress())
                .path(productURL)
                .build().toUri();
        ResponseEntity<Message<List<ProductResponse>>> exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });

        return exchange.getBody();
    }

    public Message<PageRequest<ProductResponse>> getProductsByCategory(Long categoryId, Long page, Long size) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = UriComponentsBuilder.
                fromUriString(backAdaptorProperties.getAddress())
                .path(productURL)
                .path("/category/{categoryId}")
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .expand(categoryId)
                .toUri();
        ResponseEntity<Message<PageRequest<ProductResponse>>> exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });
        return exchange.getBody();
    }

    public Message<PageRequest<ProductResponse>> getProductsBySearch(String searchWord, Long page, Long size) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = UriComponentsBuilder.
                fromUriString(backAdaptorProperties.getAddress())
                .path(productURL)
                .path("/search")
                .queryParam("searchWord", searchWord)
                .queryParam("page", page)
                .queryParam("size", size)
                .encode()
                .build()
                .toUri();
        ResponseEntity<Message<PageRequest<ProductResponse>>> exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });
        return exchange.getBody();
    }
}
