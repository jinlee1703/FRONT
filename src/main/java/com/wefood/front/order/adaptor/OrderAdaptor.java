package com.wefood.front.order.adaptor;

import com.wefood.front.config.BackAdaptorProperties;
import com.wefood.front.global.Message;
import com.wefood.front.order.dto.CartResponse;
import com.wefood.front.order.dto.ReviewGetResponse;
import com.wefood.front.order.dto.request.BasketOrderRequest;
import com.wefood.front.order.dto.request.DirectOrderCreateRequest;
import com.wefood.front.order.dto.request.OrderCreateRequest;
import com.wefood.front.order.dto.request.ReviewCreateRequest;
import com.wefood.front.order.dto.response.OrderDetailGetResponse;
import com.wefood.front.order.dto.response.OrderGetResponse;
import com.wefood.front.user.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
@RequiredArgsConstructor
public class OrderAdaptor {

    private final RestTemplate restTemplate;

    private final BackAdaptorProperties backAdaptorProperties;

    private static final String URL = "/api/orders";

    public List<ReviewGetResponse> findOrderReviewList(Long id) {

        String url = UriComponentsBuilder.fromHttpUrl(backAdaptorProperties.getAddress() + URL + "/review")
                .queryParam("userId", id)
                .toUriString();

        ResponseEntity<Message<List<ReviewGetResponse>>> responseEntity = restTemplate.exchange(
                url,
                GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return responseEntity.getBody().getData();
    }

    public ReviewGetResponse findOrderReview(Long id) {

        String url = UriComponentsBuilder.fromHttpUrl(backAdaptorProperties.getAddress() + URL + "/review-spec")
                .queryParam("id", id)
                .toUriString();

        ResponseEntity<Message<ReviewGetResponse>> responseEntity = restTemplate.exchange(
                url,
                GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return responseEntity.getBody().getData();
    }

    public List<OrderGetResponse> findOrderList(Long id) {

        String url = UriComponentsBuilder.fromHttpUrl(backAdaptorProperties.getAddress() + URL)
                .queryParam("userId", id)
                .toUriString();

        ResponseEntity<Message<List<OrderGetResponse>>> responseEntity = restTemplate.exchange(
                url,
                GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return responseEntity.getBody().getData();
    }

    public List<ReviewGetResponse> findProductReview(Long id) {

        String url = UriComponentsBuilder.fromHttpUrl(backAdaptorProperties.getAddress() + URL+"/product-review")
                .queryParam("id", id)
                .toUriString();

        ResponseEntity<Message<List<ReviewGetResponse>>> responseEntity = restTemplate.exchange(
                url,
                GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return responseEntity.getBody().getData();
    }

    public List<OrderDetailGetResponse> findOrder(Long id) {


        ResponseEntity<Message<List<OrderDetailGetResponse>>> responseEntity = restTemplate.exchange(
                backAdaptorProperties.getAddress() + URL + "/{id}",
                GET,
                null,
                new ParameterizedTypeReference<>() {
                }
                , id
        );
        return responseEntity.getBody().getData();
    }

    public void createOrder(DirectOrderCreateRequest orderCreateRequest, String id) {
        restTemplate.postForEntity(backAdaptorProperties.getAddress() + URL + "/{id}", orderCreateRequest, Void.class, id);
    }

    public void createReview(ReviewCreateRequest reviewCreateRequest, Long orderDetailId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(backAdaptorProperties.getAddress() + URL + "/review")
                .queryParam("orderDetailId", orderDetailId)
                .build()
                .toUri();

        // URI와 함께 POST 요청 전송
        restTemplate.postForEntity(uri, reviewCreateRequest, Void.class);
    }

    public void createBasketOrder(OrderCreateRequest orderCreateRequest, List<CartResponse> farms, String id) {
        restTemplate.postForEntity(backAdaptorProperties.getAddress() + URL + "/{id}/basket", new BasketOrderRequest(orderCreateRequest, farms), Void.class, id);
    }

}
