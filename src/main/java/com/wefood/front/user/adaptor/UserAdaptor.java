package com.wefood.front.user.adaptor;


import com.wefood.front.config.BackAdaptorProperties;
import com.wefood.front.global.Message;
import com.wefood.front.product.dto.ProductImageResponse;
import com.wefood.front.product.dto.UploadImageRequestDto;
import com.wefood.front.user.dto.request.*;
import com.wefood.front.user.dto.response.AddressResponse;
import com.wefood.front.user.dto.response.FarmResponse;
import com.wefood.front.user.dto.response.LoginResponse;
import com.wefood.front.user.dto.response.UserGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public void farmCreate(FarmRequest farmRequest, Long id) {
        restTemplate.postForEntity(backAdaptorProperties.getAddress() + "/api/farm/create?id={id}", farmRequest, Void.class, id);
    }

    public void farmImageCreate(MultipartFile[] multipartFiles, Long id) throws IOException {

        String url = backAdaptorProperties.getAddress() + "/api/farm/upload-part";

        // URL에 id를 쿼리 파라미터로 추가
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("id", id);

        // MultiValueMap을 사용하여 멀티파트 요청 본문 구성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // 파일들을 본문에 추가
        for (MultipartFile file : multipartFiles) {
            body.add("files", new FileSystemResource(convert(file)));
        }

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // HTTP 엔티티 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // RestTemplate을 사용하여 POST 요청 전송
        restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                requestEntity,
                Void.class
        );
    }

    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fileOutputStream = new FileOutputStream(convFile)) {
            fileOutputStream.write(file.getBytes());
        }
        return convFile;
    }

    public FarmResponse getFarm(Long id) {

        String baseUrl = backAdaptorProperties.getAddress() + "/api/farm";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("id", id);

        ResponseEntity<Message<FarmResponse>> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );


        if (responseEntity.getBody() != null) {
            return responseEntity.getBody().getData();
        } else {
            return null;
        }
    }

    public List<ProductImageResponse> getFarmImage(Long id) {

        String baseUrl = backAdaptorProperties.getAddress() + "/api/farm/image";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("id", id);

        ResponseEntity<Message<List<ProductImageResponse>>> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );


        return responseEntity.getBody().getData();
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

    public void createAddress(AddressRequest addressRequest, String id) {
        restTemplate.postForEntity(backAdaptorProperties.getAddress() + URL + "/{id}/address", addressRequest, Void.class, id);
    }

}
