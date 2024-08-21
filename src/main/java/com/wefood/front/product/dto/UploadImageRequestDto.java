package com.wefood.front.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * class: UploadImageDto.
 *
 * @author JBumLee
 * @version 2024/08/11
 */
@Getter
@Setter
@AllArgsConstructor
public class UploadImageRequestDto {
    private List<MultipartFile> files;

    private Long id;
}