package com.wefood.front.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CartResponse {

    private Long farmId;

    private String farmName;

    private List<CartProductResponse> products;

    @Builder
    public CartResponse(Long farmId, String farmName, List<CartProductResponse> products) {
        this.farmId = farmId;
        this.farmName = farmName;
        this.products = products;
    }
}
