package com.atlavik.shoppingcart.service;

import com.atlavik.shoppingcart.exception.CartNotFoundException;
import com.atlavik.shoppingcart.exception.ProductAlreadyAddedException;
import com.atlavik.shoppingcart.exception.ProductNotFoundException;
import com.atlavik.shoppingcart.model.Cart;
import com.atlavik.shoppingcart.model.Product;

import java.util.List;

public interface CartProductService {

    public List<Product> getCartProducts(String username, String cartId) throws CartNotFoundException;

    public Cart addCartProduct(String username, String cartId, String productId) throws CartNotFoundException, ProductNotFoundException, ProductAlreadyAddedException;

    public Cart removeCartProduct(String username,String cartId, String productId) throws CartNotFoundException, ProductNotFoundException;
}
