package com.atlavik.shoppingcart.service;

import com.atlavik.shoppingcart.exception.CartNotFoundException;
import com.atlavik.shoppingcart.model.Cart;

import javax.validation.Valid;

public interface CartService {
    public Cart getUserCart(String username) throws CartNotFoundException;

    public Cart addUserCart(@Valid Cart cart);

}
