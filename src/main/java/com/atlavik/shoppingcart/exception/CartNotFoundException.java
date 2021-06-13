package com.atlavik.shoppingcart.exception;


public class CartNotFoundException extends BusinessException {

    public CartNotFoundException() {
        super("Cart not Found");
    }
}
