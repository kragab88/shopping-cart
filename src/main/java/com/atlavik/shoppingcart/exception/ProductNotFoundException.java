package com.atlavik.shoppingcart.exception;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException() {
        super("Product not found");
    }
}
