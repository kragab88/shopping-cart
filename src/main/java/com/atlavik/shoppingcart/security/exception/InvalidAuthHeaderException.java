package com.atlavik.shoppingcart.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthHeaderException extends AuthenticationException {

    public InvalidAuthHeaderException(Throwable e) {
        super("Invalid authentication header", e);
    }
}
