package com.atlavik.shoppingcart.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidUsernamePasswordException extends AuthenticationException {

    public InvalidUsernamePasswordException(Throwable e){
        super("Invalid username/password",e);
    }
}
