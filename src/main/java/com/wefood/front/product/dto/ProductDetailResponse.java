package com.wefood.front.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProductDetailResponse {

    private Long id;
    private String name;
    private String detail;
    private int price;
    private Long itemId;
    private List<ImageResponse> productImg;

    private Long farmId;
    private String farmName;
    private String farmDetail;
    private List<ImageResponse> farmImg;

    @Builder
    public ProductDetailResponse(Long id, String name, String detail, int price, Long itemId, List<ImageResponse> productImg, Long farmId, String farmName, String farmDetail, List<ImageResponse> farmImg) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.price = price;
        this.itemId = itemId;
        this.productImg = productImg;
        this.farmId = farmId;
        this.farmName = farmName;
        this.farmDetail = farmDetail;
        this.farmImg = farmImg;
    }
}
