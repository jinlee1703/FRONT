package com.wefood.front.user.adaptor;


import com.wefood.front.config.BackAdaptorProperties;
import com.wefood.front.user.dto.request.SignRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.POST;

@Component
@RequiredArgsConstructor
public class UserAdaptor {

    private final RestTemplate restTemplate;

    private final BackAdaptorProperties backAdaptorProperties;
    private static final String URL = "/api/users";

    public void signUp(SignRequest request) {
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(backAdaptorProperties.getAddress() + URL, request, Void.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return;
        }
        throw new RuntimeException();
    }

}
