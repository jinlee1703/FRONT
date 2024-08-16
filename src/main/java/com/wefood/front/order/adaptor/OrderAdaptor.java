package com.wefood.front.order.adaptor;

import com.wefood.front.config.BackAdaptorProperties;
import com.wefood.front.global.Message;
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

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
@RequiredArgsConstructor
public class OrderAdaptor {

    private final RestTemplate restTemplate;

    private final BackAdaptorProperties backAdaptorProperties;

    private static final String URL = "/api/orders";


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

}
