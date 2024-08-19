package com.wefood.front.order.dto.request;

import java.time.LocalDateTime;

public record ReviewCreateRequest(String title,String content,Integer score) {
}

