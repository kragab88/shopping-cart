package com.atlavik.shoppingcart.controller;

import com.atlavik.shoppingcart.exception.CartNotFoundException;
import com.atlavik.shoppingcart.exception.ProductAlreadyAddedException;
import com.atlavik.shoppingcart.exception.ProductNotFoundException;
import com.atlavik.shoppingcart.model.Cart;
import com.atlavik.shoppingcart.model.Product;
import com.atlavik.shoppingcart.security.enitity.User;
import com.atlavik.shoppingcart.security.repository.UserRepository;
import com.atlavik.shoppingcart.security.service.JwtTokenServiceImpl;
import com.atlavik.shoppingcart.service.CartProductService;
import com.atlavik.shoppingcart.service.CartService;
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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartControllerTest {

    private static final String USER_1_VALID_TOKEN = "oyweroyeroweh9uewgfiwegfiygewicg";
    private static final String USER_1_INVALID_TOKEN = "fdssdfpiuewredfdsfdsfdsf";


    @MockBean
    private CartService cartService;


    @MockBean
    private CartProductService cartProductService;

    @SpyBean
    JwtTokenServiceImpl tokenService;
    @LocalServerPort
    private int port;

    private String url;

    @MockBean() //TODO:inject it
    private UserRepository userRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void setupTokenApiURL() {

        url = String.format("http://localhost:%d/v1/api/carts", port);
    }



    @Test
    @DisplayName("Get user cart for anonymous user")
    public void getCart_ShouldReturn_NotAuthorizedUser() {

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<String>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Get user cart for user1 with valid token")
    public void getCart_ShouldReturn_User1Cart_ValidToken() throws CartNotFoundException {
        restTemplate = getUser1ValidTokenTemplate();
        Cart cart = new Cart();
        cart.setId(UUID.fromString("8400000-8cf0-11bd-b23e-6666699"));
        Mockito.when(cartService.getUserCart("user1")).thenReturn(cart);
        ResponseEntity<Cart> response = restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<Cart>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        Assertions.assertThat(((Cart) response.getBody()).getId()).isEqualByComparingTo(UUID.fromString("8400000-8cf0-11bd-b23e-6666699"));
    }

    @Test
    @DisplayName("Get user cart for user1 with invalid token")
    public void getCart_ShouldReturn_User1Cart_InvalidToken() {
        User user = new User();
        user.setUsername("user1");
        user.setActive(true);
        user.setPassword("password");
        Mockito.when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        Mockito.doReturn("user1").when(tokenService).getUsernameFromToken(USER_1_INVALID_TOKEN);
        Mockito.doReturn(false).when(tokenService).isValidToken(USER_1_INVALID_TOKEN);

        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("Authorization", "Bearer " + USER_1_INVALID_TOKEN);
                    return execution.execute(request, body);
                }));

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<String>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
    }


    @Test
    @DisplayName("get user1 cart however no carts created for user1")
    public void getCart_ShouldReturn_noCarts() throws CartNotFoundException {
        restTemplate = getUser1ValidTokenTemplate();
        Mockito.when(cartService.getUserCart("user1")).thenThrow(new CartNotFoundException());
          ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<String>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat((String) response.getBody()).contains("Cart not Found");
    }

    @Test
    @DisplayName("Add cart to user1, cart created successfully")
    public void AddUserCart_ShouldReturn_CartCreated() {
        restTemplate = getUser1ValidTokenTemplate();
        Cart cart = new Cart();
        cart.setId(UUID.fromString("08400000-8cf0-11bd-b23e-000006666699"));
        Mockito.when(cartService.addUserCart(org.mockito.ArgumentMatchers.any(Cart.class))).thenReturn(cart);
         HttpEntity<Cart> cartRequest = new HttpEntity<Cart>(cart);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,
                cartRequest, new ParameterizedTypeReference<String>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        Assertions.assertThat(((String) response.getBody())).contains("08400000-8cf0-11bd-b23e-000006666699");
    }


    @Test
    @DisplayName("Add product to user1 cart, cart exists, product available, product added successfully")
    public void AddUserCart_ShouldReturn_ProductAdded() throws CartNotFoundException, ProductAlreadyAddedException, ProductNotFoundException {
        restTemplate = getUser1ValidTokenTemplate();
        Cart cart = new Cart();
        cart.setId(UUID.fromString("08400000-8cf0-11bd-b23e-000006666699"));
        Mockito.when(cartProductService.addCartProduct("user1", "08400000-8cf0-11bd-b23e-000006666699", "08400000-8cf0-11bd-b23e-000006666633")).thenReturn(cart);
        ResponseEntity<String> response = restTemplate.exchange(url + "/{cartId}/product/{productId}", HttpMethod.PUT, null, new ParameterizedTypeReference<String>() {}, "08400000-8cf0-11bd-b23e-000006666699", "08400000-8cf0-11bd-b23e-000006666633");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }


    @Test
    @DisplayName("remove user1 product, cart exists, product available, product removed successfully")
    public void removeUserCart_ShouldReturn_ProductRemoved() throws CartNotFoundException, ProductNotFoundException {
        restTemplate = getUser1ValidTokenTemplate();
        Cart cart = new Cart();
        cart.setId(UUID.fromString("08400000-8cf0-11bd-b23e-000006666699"));
        Mockito.when(cartProductService.removeCartProduct("user1", "08400000-8cf0-11bd-b23e-000006666699", "08400000-8cf0-11bd-b23e-000006666633")).thenReturn(cart);
        ResponseEntity<String> response = restTemplate.exchange(url + "/{cartId}/product/{productId}", HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {}, "08400000-8cf0-11bd-b23e-000006666699", "08400000-8cf0-11bd-b23e-000006666633");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }


    @Test
    @DisplayName("get user1 car products, cart exists, product available")
    public void getCartProducts_ShouldReturn_ListOfProducts() throws CartNotFoundException {
        restTemplate = getUser1ValidTokenTemplate();
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

        Mockito.when(cartProductService.getCartProducts("user1", "08400000-8cf0-11bd-b23e-000006666699")).thenReturn(products);
        ResponseEntity<List<Product>> response = restTemplate.exchange(url + "/{cartId}/product", HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {},"08400000-8cf0-11bd-b23e-000006666699");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }


    private TestRestTemplate getUser1ValidTokenTemplate(){
        User user = new User();
        user.setUsername("user1");
        user.setActive(true);
        user.setPassword("password");
        Mockito.when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        Mockito.doReturn("user1").when(tokenService).getUsernameFromToken(USER_1_VALID_TOKEN);
        Mockito.doReturn(true).when(tokenService).isValidToken(USER_1_VALID_TOKEN);

        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("Authorization", "Bearer " + USER_1_VALID_TOKEN);
                    return execution.execute(request, body);
                }));
        return restTemplate;
    }
}
