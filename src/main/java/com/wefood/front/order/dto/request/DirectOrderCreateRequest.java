package com.wefood.front.order.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DirectOrderCreateRequest(Integer totalCost, String invoiceNumber, String receiverPhoneNumber,
                                       String receiverName, String receiverAddress, String receiverAddressDetail,
                                       String deliveryDate, String meetingAt, Long productId, Integer quantity,
                                       Integer price) {
}
