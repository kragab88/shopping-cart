package com.atlavik.shoppingcart.exception;

public class ProductAlreadyAddedException extends BusinessException {


    public ProductAlreadyAddedException() {
        super("The product already added before");
    }
}
