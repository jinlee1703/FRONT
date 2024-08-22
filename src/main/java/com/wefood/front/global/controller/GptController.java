package com.wefood.front.global.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.wefood.front.global.service.GptService;
import com.wefood.front.global.dto.GptModelsResponseDto;
import com.wefood.front.global.dto.GptResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * class: ChatGptController.
 *
 * @author JBumLee
 * @version 2024/08/21
 */
@RestController
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
public class GptController {

    @Value("${openai.endpoint.gpt-free}")
    private String endpointFree;

    @Value("${openai.endpoint.gpt-charged}")
    private String endpointCharged;

    private final GptService chatGPTService;

    @GetMapping("/models")
    public ResponseEntity<GptModelsResponseDto> selectModelList() {
        return ResponseEntity.ok().body(chatGPTService.models());
    }

    @PostMapping("/chat")
    public String chat(
        @NotNull @RequestParam("model") String model,
        @NotNull @RequestParam("isProduct") boolean isProduct,
        Model view, @NotNull @RequestParam("input") String... input) {

        // 서비스 호출

        // 필요한 데이터를 모델에 추가
        view.addAttribute("gpt",
            chatGPTService.chat(model, endpointCharged, isProduct, input));  // 추가 데이터 예시
        if(isProduct) return "fragments/modalContent :: modalContentFragment";  // 타임리프 조각을 반환
        return "farm 알아서 처리하세요";
    }
}