package com.wefood.front.global.dto;

import java.util.List;
import lombok.Getter;

/**
 * class: GptResponseDto.
 *
 * @author JBumLee
 * @version 2024/08/21
 */

public record GptResponseDto(String completion, String created, String id, String model,
                             String object, List<Choice> choices) {
}