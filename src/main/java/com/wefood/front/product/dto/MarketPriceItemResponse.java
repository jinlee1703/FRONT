package com.wefood.front.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketPriceItemResponse {

    // 품목명
    private String item_name;
    // 품종명
    private String kind_name;
    // 상품 등급
    private String rank;
    // 하루 전 가격
    private String dpr2;
}
