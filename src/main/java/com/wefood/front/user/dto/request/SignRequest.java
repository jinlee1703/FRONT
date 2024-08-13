package com.wefood.front.user.dto.request;

public record SignRequest(String name, String phoneNumber, String password, Boolean isSeller, String accountNumber) {

}
