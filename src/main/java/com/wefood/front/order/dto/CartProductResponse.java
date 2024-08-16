package com.wefood.front.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartProductResponse {

    private Long id;

    private String name;

    private int price;

    private String thumbnail;

    private int quantity;

    @Builder
    public CartProductResponse(Long id, String name, int price, String thumbnail, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.thumbnail = thumbnail;
        this.quantity = quantity;
    }
}
