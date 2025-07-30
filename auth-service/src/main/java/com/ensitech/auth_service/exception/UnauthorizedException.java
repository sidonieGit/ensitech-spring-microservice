package com.ensitech.auth_service.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {

        super(message);
        System.out.println("UnauthorizedException "+message);
    }
}
