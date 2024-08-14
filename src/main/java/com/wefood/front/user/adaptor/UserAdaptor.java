package com.wefood.front.user.adaptor;


import com.wefood.front.config.BackAdaptorProperties;
import com.wefood.front.global.Message;
import com.wefood.front.user.dto.request.LoginRequest;
import com.wefood.front.user.dto.request.SignRequest;
import com.wefood.front.user.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.POST;

@Component
@RequiredArgsConstructor
public class UserAdaptor {

    private final RestTemplate restTemplate;

    private final BackAdaptorProperties backAdaptorProperties;
    private static final String URL = "/api/users";

    public void signUp(SignRequest signRequest) {
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(backAdaptorProperties.getAddress() + URL, signRequest, Void.class);

        if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.CREATED)) {
            return;
        }
        throw new RuntimeException();
    }

    public LoginResponse login(LoginRequest loginRequest) {

        ResponseEntity<Message<LoginResponse>> responseEntity = restTemplate.exchange(
                backAdaptorProperties.getAddress() + URL + "/login",
                HttpMethod.POST,
                new HttpEntity<>(loginRequest),
                new ParameterizedTypeReference<Message<LoginResponse>>() {}
        );

        return responseEntity.getBody().getData();
    }

}
