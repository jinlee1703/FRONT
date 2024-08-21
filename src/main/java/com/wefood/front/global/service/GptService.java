package com.wefood.front.global.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wefood.front.global.dto.GptMessage;
import com.wefood.front.global.dto.GptModelsResponseDto;
import com.wefood.front.global.dto.GptRequestDto;
import com.wefood.front.global.dto.GptResponseDto;

import java.util.List;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * class: ChatGptService.
 *
 * @author JBumLee
 * @version 2024/08/21
 */
@Slf4j
@Service
public class GptService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private String GPT_PRODUCT = "1. 응답형태 = data:{name:String, detail:String, tag:List<String>} 형태의 json을 원함 바로 자바에서 dto로 형변환 할 수 있게 \\n 2. name = 상품의 이름이야. 너에게 원하는건 농가이름과 상품의 강조하고싶은 점 + 이쁘게 여러형용사 사용 \\n 3. detail = 상품의 소개야. 강조하고싶은 점과 너의 생각으로 자세하게 작성해주면 좋겟어(100자)_\\n 4. tags = 강조하고싶은 점을 이용하여 어울리는 태그를 만들어줘 \\n 5. 상품 품종 = %s \\n 6. 강조하고 싶은점 = %s \\n 7. 농가 이름 = %s";

    private String GPT_FARM = "[data : %s]  1. data를 이용 , 2.  농산품을 판매하는 농가의 소개를 작성 , 3. 사람에게 말하듯 친숙한 말투로 생성 , 4.  완성된 응답은 detail:String 형태의 json 의 형태로 반환.";

    public GptService(@Qualifier("gptRestTemplate") RestTemplate restTemplate,
                      ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public GptModelsResponseDto models() {
        String url = "https://api.openai.com/v1/models";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        GptModelsResponseDto gptModelsResponseDto = null;
        try {
            gptModelsResponseDto = objectMapper.readValue(response.getBody(),
                    GptModelsResponseDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing response from OpenAI Server", e);
        }
        return gptModelsResponseDto;
    }

    public JsonNode chat(String model, String endpointCharged, boolean isProduct,
                         String... inputs) {


        String prompt = preprocessing(isProduct, inputs);
        List<GptMessage> prompts = List.of(new GptMessage("user", prompt));
        GptRequestDto request = new GptRequestDto(model, prompts, 1, 256, 1, 0, 0);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GptRequestDto> entity = new HttpEntity<>(request, headers);

        // OpenAI server로 restTemplate을 통해 request를 보내고 response를 받는다.
        GptResponseDto gptResponse = restTemplate.exchange(
                endpointCharged, HttpMethod.POST, entity, GptResponseDto.class).getBody();
        if (gptResponse != null) {
            String string = gptResponse.choices().get(0).message().content();
            return stringToProductJson(string);
        } else {
            throw new RuntimeException("Error parsing response from OpenAI Server");
        }
    }

    private String preprocessing(boolean isProduct, String... inputs) {
        if (isProduct) {
            return GPT_PRODUCT.formatted(inputs);
        }
        return GPT_FARM.formatted(inputs);
    }

    private JsonNode stringToProductJson(String text) {
        try {

            // "json" 이후의 실제 JSON 부분만 추출
            log.info(text);

            String jsonPart = text.substring(text.indexOf("{"));

            // ObjectMapper를 사용해 문자열을 JsonNode로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonPart);

            // 개별 필드 파싱
            String name = jsonNode.get("name").asText();
            String detail = jsonNode.get("detail").asText();
            JsonNode tagsNode = jsonNode.get("tags");

            // 태그들을 String 배열로 변환
            String[] tags = objectMapper.convertValue(tagsNode, String[].class);

            // 파싱된 값 출력
            log.info("Name: " + name);
            log.info("Detail: " + detail);
            log.info("Tags: ");
            for (String tag : tags) {
                log.info(tag + " ");
            }
            return jsonNode;
        } catch (Exception e) {
            log.error(e.toString());
        }
        throw new IllegalArgumentException("예상치 못한 에러발생");
    }

    public String farmChat(String model, String endpointCharged, boolean isProduct,
                         String... inputs) {


        String prompt = preprocessing(isProduct, inputs);

        System.out.println("프롬포트@?@?@?@#?#?#?#?#?#");
        System.out.println(prompt);

        List<GptMessage> prompts = List.of(new GptMessage("user", prompt));
        GptRequestDto request = new GptRequestDto(model, prompts, 1, 256, 1, 0, 0);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GptRequestDto> entity = new HttpEntity<>(request, headers);

        // OpenAI server로 restTemplate을 통해 request를 보내고 response를 받는다.
        GptResponseDto gptResponse = restTemplate.exchange(
                endpointCharged, HttpMethod.POST, entity, GptResponseDto.class).getBody();
        if (gptResponse != null) {
            String string = gptResponse.choices().get(0).message().content();
            return stringToFarmJson(string);
        } else {
            throw new RuntimeException("Error parsing response from OpenAI Server");
        }
    }

    private String stringToFarmJson(String text) {
        try {


            // "json" 이후의 실제 JSON 부분만 추출
            log.info(text);

            String jsonPart = text.substring(text.indexOf("{"));
            // ObjectMapper를 사용해 문자열을 JsonNode로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonPart);

            String detail = jsonNode.get("detail").asText();
            log.info("Detail: " + detail);

            return detail;
        } catch (Exception e) {
            log.error(e.toString());
        }
        throw new IllegalArgumentException("예상치 못한 에러발생");
    }
}