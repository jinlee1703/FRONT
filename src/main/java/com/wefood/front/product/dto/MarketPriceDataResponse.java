package com.wefood.front.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MarketPriceDataResponse {

    private String error_code;
    private List<MarketPriceItemResponse> item;
}
