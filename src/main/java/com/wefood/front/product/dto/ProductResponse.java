package com.wefood.front.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private String img;

    @Builder
    public ProductResponse(Long id, String name, int price, String img) {
        this.id = id;
        this.name= name;
        this.price = price;
        this.img = img;
    }
}
