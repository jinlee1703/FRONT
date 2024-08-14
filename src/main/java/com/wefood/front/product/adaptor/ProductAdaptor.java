package com.wefood.front.product.adaptor;

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

    public ProductAdaptor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Message<ProductDetailResponse> getProductDetail(Long productId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Message<ProductDetailResponse>> exchange = restTemplate.exchange("http://localhost:8081/api/product/" + productId, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });

        return exchange.getBody();
    }

    public Message<List<ProductResponse>> getProducts() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Message<List<ProductResponse>>> exchange = restTemplate.exchange("http://localhost:8081/api/product", HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });

        return exchange.getBody();
    }

    public Message<PageRequest<ProductResponse>> getProductsByCategory(Long categoryId, Long page, Long size) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = UriComponentsBuilder.
                fromUriString("http://localhost:8081/api/product/category")
                .path("/{categoryId}")
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
                fromUriString("http://localhost:8081/api/product/search")
                .queryParam("searchWord", searchWord)
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .toUri();
        ResponseEntity<Message<PageRequest<ProductResponse>>> exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });
        return exchange.getBody();
    }
}
