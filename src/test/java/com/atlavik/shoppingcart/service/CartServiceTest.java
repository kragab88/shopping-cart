package com.atlavik.shoppingcart.service;

import com.atlavik.shoppingcart.exception.CartNotFoundException;
import com.atlavik.shoppingcart.model.Cart;
import com.atlavik.shoppingcart.repository.CartRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import javax.validation.ConstraintViolationException;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @MockBean
    CartRepository cartRepository;
    @Autowired
    CartService cartService;


    @Test
    public void shouldReturn_Cart() throws CartNotFoundException {
        Mockito.when(cartRepository.findByUsername("user1"))
                .thenReturn(java.util.Optional.of(new Cart()));
        Assertions.assertThat(cartService.getUserCart("user1")).isNotNull();
    }


    @Test
    public void shouldThrow_CartNotFoundException_noCartForthatUser() {
        Mockito.when(cartRepository.findByUsername("user2"))
                .thenReturn(java.util.Optional.empty());
        try {
            cartService.getUserCart("user2");
            Assertions.fail("Should throw an exception: Cart not Found");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(CartNotFoundException.class)
                    .hasMessageContaining("Cart not Found");
        }
    }


    @Test
    public void shouldReturn_CartIdAndSaveTheCart() {
        Mockito.when(cartRepository.save(Mockito.any(Cart.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        Cart cart = new Cart();
        String uuidStr = "38400000-8cf0-11bd-b23e-10b96e4ef00d";
        UUID uid = UUID.fromString(uuidStr);
        cart.setId(uid);
        cart.setUsername("user1");
        cart.setCountryCode("US");
        cart.setCurrency("USD");
        Assertions.assertThat(cartService.addUserCart(cart).getId()).isEqualTo(uid);
    }

    @Test
    public void shouldThrow_ConstraintViolation_duplicatedUserName() {
        Mockito.when(cartRepository.existsByUsername("user2"))
                .thenReturn(true);

        try {
            Cart cart = new Cart();
            cart.setUsername("user2");
            cart.setCountryCode("US");
            cart.setCurrency("USD");
            cartService.addUserCart(cart);
            Assertions.fail("Should throw an exception: ConstraintViolation ");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("There is a cart already created");
        }
    }


    @Test
    public void shouldThrow_ConstraintViolation_EmptyCurrency() {
        Mockito.when(cartRepository.existsByUsername("user2"))
                .thenReturn(false);
        try {
            Cart cart = new Cart();
            cart.setUsername("user2");
            cart.setCountryCode("US");
            cart.setCurrency(null);
            cartService.addUserCart(cart);
            Assertions.fail("Should throw an exception: ConstraintViolation ");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("Currency cannot be empty");
        }
    }

    @Test
    public void shouldThrow_ConstraintViolation_EmptyCountry() {
        Mockito.when(cartRepository.existsByUsername("user2"))
                .thenReturn(false);
        try {
            Cart cart = new Cart();
            cart.setUsername("user2");
            cart.setCountryCode(null);
            cart.setCurrency("USD");
            cartService.addUserCart(cart);
            Assertions.fail("Should throw an exception: ConstraintViolation ");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("Country Code cannot be empty");
        }
    }

}
