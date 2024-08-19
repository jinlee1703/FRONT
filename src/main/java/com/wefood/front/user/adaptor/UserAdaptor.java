package com.wefood.front.user.adaptor;


import com.wefood.front.config.BackAdaptorProperties;
import com.wefood.front.global.Message;
import com.wefood.front.user.dto.request.AddressRequest;
import com.wefood.front.user.dto.request.LoginRequest;
import com.wefood.front.user.dto.request.SignRequest;
import com.wefood.front.user.dto.request.UserGetRequest;
import com.wefood.front.user.dto.response.AddressResponse;
import com.wefood.front.user.dto.response.LoginResponse;
import com.wefood.front.user.dto.response.UserGetResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;
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
                POST,
                new HttpEntity<>(loginRequest),
                new ParameterizedTypeReference<>() {
                }
        );

        return responseEntity.getBody().getData();
    }

    public UserGetResponse findUser(UserGetRequest userGetRequest) {

        ResponseEntity<Message<UserGetResponse>> responseEntity = restTemplate.exchange(
                backAdaptorProperties.getAddress() + URL + "/mypage",
                POST,
                new HttpEntity<>(userGetRequest),
                new ParameterizedTypeReference<>() {
                }
        );
        return responseEntity.getBody().getData();
    }

    public AddressResponse findAddress(Long userId) {

        ResponseEntity<Message<AddressResponse>> responseEntity = restTemplate.exchange(
                backAdaptorProperties.getAddress() + URL + "/address/{id}",
                GET,
                null,
                new ParameterizedTypeReference<>() {
                }, userId
        );

        return responseEntity.getBody().getData();
    }

    public void createAddress(AddressRequest addressRequest, String  id) {
        restTemplate.postForEntity(backAdaptorProperties.getAddress() + URL + "/{id}/address", addressRequest, Void.class, id);
    }

}
