package com.wefood.front.global.dto;

import java.util.List;
import lombok.Getter;

/**
 * class: GptRequestDto.
 *
 * @author JBumLee
 * @version 2024/08/21
 */
public record GptRequestDto(String model, List<GptMessage> messages,
                            float temperature, int max_tokens, int top_p,
                            int frequency_penalty, int presence_penalty) {
}