package com.atlavik.shoppingcart.error;

import java.util.List;

public class WrongInputsError {

    private String message;
    private List<String> errors;

    public WrongInputsError(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }

}
