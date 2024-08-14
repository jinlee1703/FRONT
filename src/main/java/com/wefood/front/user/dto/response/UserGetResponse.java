package com.wefood.front.user.dto.response;

public record UserGetResponse(String phoneNumber, String password, String name, Boolean isSeller, Boolean isResign,
                              String accountNumber) {
}
