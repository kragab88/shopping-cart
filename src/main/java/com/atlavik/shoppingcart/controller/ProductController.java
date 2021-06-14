package com.atlavik.shoppingcart.controller;

import com.atlavik.shoppingcart.model.Product;
import com.atlavik.shoppingcart.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "v1/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @ApiOperation(value = "get all products")
    @GetMapping(produces = "application/json")
    public ResponseEntity getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products != null && products.size() > 0)
            return new ResponseEntity(products, HttpStatus.OK);

        return new ResponseEntity("No Products found", HttpStatus.NOT_FOUND);
    }

}
