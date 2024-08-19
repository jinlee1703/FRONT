package com.wefood.front.order.dto.request;


public record OrderCreateRequest(Integer totalCost, String invoiceNumber, String receiverPhoneNumber,
                                       String receiverName, String receiverAddress, String receiverAddressDetail,
                                       String deliveryDate, String meetingAt) {
}