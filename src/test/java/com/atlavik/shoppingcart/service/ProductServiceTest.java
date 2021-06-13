package com.atlavik.shoppingcart.service;

import com.atlavik.shoppingcart.model.Product;
import com.atlavik.shoppingcart.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @MockBean
    ProductRepository productRepository;
    @Autowired
    ProductService productService;

    @Test
    public void shouldReturn_ListOfProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());
        Mockito.when(productRepository.findAll()).thenReturn(products);
        Assertions.assertThat(productService.getAllProducts()).isInstanceOf(List.class);
        Assertions.assertThat(productService.getAllProducts()).isNotNull();
        Assertions.assertThat(productService.getAllProducts().size()).isEqualTo(2);

    }
}
