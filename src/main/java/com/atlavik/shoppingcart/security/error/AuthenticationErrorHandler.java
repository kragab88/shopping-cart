package com.atlavik.shoppingcart.security.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class AuthenticationErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            AuthenticationException ex, WebRequest request) {
        return new ResponseEntity(ex.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
    }

}
