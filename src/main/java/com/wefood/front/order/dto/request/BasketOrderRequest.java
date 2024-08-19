package com.wefood.front.order.dto.request;

import com.wefood.front.order.dto.CartResponse;

import java.util.List;

public record BasketOrderRequest(OrderCreateRequest orderCreateRequest, List<CartResponse> farms) {
}
