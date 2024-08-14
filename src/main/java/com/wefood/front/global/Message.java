package com.wefood.front.global;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message<T> {
    private int statusCode;
    private String message;
    private T data;

    public Message(int statusCode, String message, T data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }

    public Message(int statusCode, String message) {
        this(statusCode, message, null);
    }
}
