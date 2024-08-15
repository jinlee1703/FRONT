package com.wefood.front.product.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductImageResponse {
    private String name;
    private Boolean isThumbnail;

    @Builder
    public ProductImageResponse(String name, Boolean isThumbnail) {
        this.name = name;
        this.isThumbnail = isThumbnail;
    }
}