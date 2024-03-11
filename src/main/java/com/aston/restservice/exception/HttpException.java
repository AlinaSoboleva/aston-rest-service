package com.aston.restservice.exception;

public class HttpException extends RuntimeException {
    public HttpException(String message) {
        super(message);
    }
}
