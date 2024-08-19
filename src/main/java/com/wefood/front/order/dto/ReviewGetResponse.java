package com.wefood.front.order.dto;

public record ReviewGetResponse(Long id, Long orderDetailId, Long productId, String title, String content,
                                Integer score) {
}
