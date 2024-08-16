package com.wefood.front.product.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductImageResponse {
    private String img;
    private Boolean isThumbnail;
    private Integer sequence;

    @Builder
    public ProductImageResponse(String img, Boolean isThumbnail, Integer sequence) {
        this.img = img;
        this.isThumbnail = isThumbnail;
        this.sequence = sequence;
    }
}