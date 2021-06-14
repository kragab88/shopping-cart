package com.atlavik.shoppingcart.service;

import com.atlavik.shoppingcart.exception.CartNotFoundException;
import com.atlavik.shoppingcart.exception.ProductAlreadyAddedException;
import com.atlavik.shoppingcart.exception.ProductNotFoundException;
import com.atlavik.shoppingcart.model.Cart;
import com.atlavik.shoppingcart.model.Product;
import com.atlavik.shoppingcart.repository.CartRepository;
import com.atlavik.shoppingcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Service
@Validated
public class CartServiceImpl implements CartService, CartProductService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart getUserCart(String username) throws CartNotFoundException {

        return cartRepository.findByUsername(username).orElseThrow(() -> new CartNotFoundException());
    }

    public Cart addUserCart(@Valid Cart cart) {
        Cart addedCart = cartRepository.save(cart);
        return addedCart;
    }

    @Override
    public List<Product> getCartProducts(String username, String cartId) throws CartNotFoundException {
        Cart cart = cartRepository.findByIdAndUsername(cartId, username).orElseThrow(() -> new CartNotFoundException());
        return cart.getProducts();
    }

    @Override
    public Cart addCartProduct(String username, String cartId, String productId) throws CartNotFoundException, ProductNotFoundException, ProductAlreadyAddedException {
        Cart cart = cartRepository.findByIdAndUsername(cartId, username).orElseThrow(() -> new CartNotFoundException());
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException());
        if (cart.getProducts().contains(product))
            throw new ProductAlreadyAddedException();
        cart.getProducts().add(product);
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeCartProduct(String username, String cartId, String productId) throws CartNotFoundException, ProductNotFoundException {
        Cart cart = cartRepository.findByIdAndUsername(cartId, username).orElseThrow(() -> new CartNotFoundException());
        Product product = new Product();
        product.setId(UUID.fromString(productId));
        if (!cart.getProducts().contains(product))
            throw new ProductNotFoundException();
        cart.getProducts().remove(product);
        return cartRepository.save(cart);
    }


}
