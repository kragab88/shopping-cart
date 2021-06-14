package com.atlavik.shoppingcart.controller;

import com.atlavik.shoppingcart.model.Product;
import com.atlavik.shoppingcart.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @LocalServerPort
    private int port;

    private String url;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void setupTokenApiURL() {

        url = String.format("http://localhost:%d/v1/api/products", port);
    }


    @Test
    @DisplayName("Get all available products for anonymous user")
    public void getAllProducts_ShouldReturn_ProductList() {

        List<Product> products = new ArrayList<>();
        Product p1 = new Product();
        p1.setId(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"));
        p1.setDescription("Phone");
        p1.setCategory("Phones");
        products.add(p1);

        Product p2 = new Product();
        p2.setId(UUID.fromString("38400000-8cf0-11bd-b23e-44445"));
        p2.setDescription("Headphone1");
        p2.setCategory("Headphones");
        products.add(p2);

        Mockito.when(productService.getAllProducts()).thenReturn(products);
        ResponseEntity<List<Product>> response = restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Product>>() {
                });
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        Assertions.assertThat((List<Product>) response.getBody()).isInstanceOf(List.class);
        List<Product> responseBody = (List<Product>) response.getBody();

        Assertions.assertThat(responseBody.size()).isEqualTo(2);
        Assertions.assertThat(responseBody.get(0).getDescription()).isEqualTo("Phone");
    }


    @Test
    @DisplayName("Get all available products for anonymous user however there are no products available")
    public void getAllProducts_ShouldReturn_NoProductsFound() {
        Mockito.when(productService.getAllProducts()).thenReturn(new ArrayList<>());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<String>() {
                });
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat((String) response.getBody()).isEqualTo("No Products found");
    }
}
