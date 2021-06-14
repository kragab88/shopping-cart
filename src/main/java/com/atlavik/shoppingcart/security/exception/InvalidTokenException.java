package com.atlavik.shoppingcart.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException(Throwable e) {
        super("Invalid token", e);
    }

    public InvalidTokenException() {
        super("Invalid token");
    }
}
