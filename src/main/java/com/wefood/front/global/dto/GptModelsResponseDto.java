package com.wefood.front.global.dto;

import java.util.List;

/**
 * class: GptModelsResponseDto.
 *
 * @author JBumLee
 * @version 2024/08/21
 */
public record GptModelsResponseDto(String object, List<ModelData> data) {
}
