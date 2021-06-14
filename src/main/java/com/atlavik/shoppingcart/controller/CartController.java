package com.atlavik.shoppingcart.controller;

import com.atlavik.shoppingcart.exception.CartNotFoundException;
import com.atlavik.shoppingcart.exception.ProductAlreadyAddedException;
import com.atlavik.shoppingcart.exception.ProductNotFoundException;
import com.atlavik.shoppingcart.model.Cart;
import com.atlavik.shoppingcart.model.Product;
import com.atlavik.shoppingcart.service.CartProductService;
import com.atlavik.shoppingcart.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@Api(authorizations = {@Authorization(value = "BearerAuthOAuth")})
@RestController
@RequestMapping(value = "v1/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartProductService cartProductService;

    @ApiOperation(value = "Get All Carts")
    @GetMapping(produces = "application/json")
    public ResponseEntity getCarts() throws CartNotFoundException {

        Cart cart = cartService.getUserCart(getLoggedInUsername());
            return new ResponseEntity(cart,HttpStatus.OK);
    }

    @ApiOperation(value = "add new Cart")
    @PostMapping(produces = "application/json",consumes = "application/json")
    public ResponseEntity addCart( @RequestBody Cart cart) {
        cart.setUsername(getLoggedInUsername());
        Cart addedCart = cartService.addUserCart(cart);
        return new ResponseEntity("Cart Added ID: " + addedCart.getId().toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "get products for a cart using cartId")
    @GetMapping(value = "{cartId}/product",produces = "application/json")
    public ResponseEntity getCartProducts(@PathVariable String cartId) throws CartNotFoundException {
        List<Product> products = cartProductService.getCartProducts(getLoggedInUsername(),cartId);
        if (products.size() > 0)
                return new ResponseEntity(products, HttpStatus.OK);
        return new ResponseEntity("No Products Available", HttpStatus.NOT_FOUND);
}

    @ApiOperation(value = "add product to a cart using cartId and productId")
    @PutMapping(value = "{cartId}/product/{productId}",produces = "application/json")
    public ResponseEntity addCartProduct(@PathVariable String cartId, @PathVariable String productId) throws CartNotFoundException, ProductAlreadyAddedException, ProductNotFoundException {
        cartProductService.addCartProduct(getLoggedInUsername(),cartId, productId);
        return new ResponseEntity("Product Added Successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "remove product from a cart using cartId and productId")
    @DeleteMapping(value = "{cartId}/product/{productId}",produces = "application/json")
    public ResponseEntity removeCartProduct(@PathVariable String cartId, @PathVariable String productId) throws CartNotFoundException, ProductNotFoundException {
        cartProductService.removeCartProduct(getLoggedInUsername(),cartId,productId);
        return new ResponseEntity("Product Removed Successfully", HttpStatus.OK);
    }

    private String getLoggedInUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return  userDetails.getUsername();
    }
}
