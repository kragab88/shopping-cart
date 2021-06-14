package com.atlavik.shoppingcart.service;

import com.atlavik.shoppingcart.exception.CartNotFoundException;
import com.atlavik.shoppingcart.exception.ProductAlreadyAddedException;
import com.atlavik.shoppingcart.exception.ProductNotFoundException;
import com.atlavik.shoppingcart.model.Cart;
import com.atlavik.shoppingcart.model.Product;
import com.atlavik.shoppingcart.repository.CartRepository;
import com.atlavik.shoppingcart.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartProductServiceTest {

    public static final String CART_ID = "8400000-8cf0-11bd-b23e-6666655";
    public static final String PRODUCT_REPOSITORY_ID = "38400000-8cf0-11bd-b23e-6666641";
    public static final String PRODUCT_CART_1_ID = "8400000-8cf0-11bd-b23e-6666633";
    public static final String PRODUCT_CART_2_ID = "8400000-8cf0-11bd-b23e-6666622";

    @MockBean
    CartRepository cartRepository;
    @InjectMocks
    Cart cart;
    @Autowired
    CartProductService cartService;
    @MockBean
    ProductRepository productRepository;


    public Cart getCartEntity() {

        List<Product> products = new ArrayList<>();
        Product p1 = new Product();
        p1.setId(UUID.fromString(PRODUCT_CART_1_ID));
        p1.setDescription("Phone");
        p1.setCategory("Phones");
        products.add(p1);

        Product p2 = new Product();
        p2.setId(UUID.fromString(PRODUCT_CART_2_ID));
        p2.setDescription("Headphone1");
        p2.setCategory("Headphones");
        products.add(p2);
        Cart cart = new Cart();
        cart.setId(UUID.fromString(CART_ID));
        cart.setProducts(products);
        return cart;
    }

    @BeforeEach
    public void setupCartRepository() {
        Mockito.when(cartRepository.findByIdAndUsername(CART_ID, "user1"))
                .thenReturn(java.util.Optional.of(getCartEntity()));
        Mockito.when(cartRepository.save(org.mockito.ArgumentMatchers.any(Cart.class))).thenAnswer(new Answer<Cart>() {
            @Override
            public Cart answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (Cart) args[0];
            }
        });

    }

    @BeforeEach
    public void setupProductRepository() {
        Product p1 = new Product();
        p1.setId(UUID.fromString(PRODUCT_REPOSITORY_ID));
        p1.setDescription("Phone");
        p1.setCategory("Phones");
        Mockito.when(productRepository.findById(PRODUCT_REPOSITORY_ID))
                .thenReturn(java.util.Optional.of(p1));

        Product productExist = new Product();
        productExist.setId(UUID.fromString(PRODUCT_CART_2_ID));
        productExist.setDescription("Phone2");
        productExist.setCategory("Phones");
        Mockito.when(productRepository.findById(PRODUCT_CART_2_ID))
                .thenReturn(java.util.Optional.of(productExist));
    }

    @Test
    @DisplayName("Return list of products using username and existing cart Id")
    public void getProducts_shouldReturn_ListOfProducts() throws CartNotFoundException {

        Assertions.assertThat(cartService.getCartProducts("user1", CART_ID).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Get cart with user name and that user hasn't cart")
    public void getProducts_shouldThrow_NoCartFound_NoCartForThatUser() {

        try {
            cartService.getCartProducts("user2", CART_ID);
            Assertions.fail("Should throw an exception: Cart not found ");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(CartNotFoundException.class)
                    .hasMessage("Cart not Found");
        }
    }

    @Test
    @DisplayName("Get cart with wrong Id ")
    public void getProducts_shouldThrow_NoCartFound_NoCartwithThatId() {
        try {
            cartService.getCartProducts("user1", "8273847-9884-999-444");
            Assertions.fail("Should throw an exception: Cart not found ");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(CartNotFoundException.class)
                    .hasMessage("Cart not Found");
        }
    }


    @Test
    @DisplayName("Add product to existing cart")
    public void addProduct_shouldAddProduct() throws CartNotFoundException, ProductAlreadyAddedException, ProductNotFoundException {
        Product p = new Product();
        p.setId(UUID.fromString(PRODUCT_REPOSITORY_ID));
        List<Product> products = cartService.addCartProduct("user1", CART_ID, PRODUCT_REPOSITORY_ID).getProducts();
        Assertions.assertThat(products).contains(p);
        Assertions.assertThat(products.get(0).getDescription()).isEqualTo("Phone");
    }

    @Test
    @DisplayName("Try to Add product is not one of the available products")
    public void addProduct_shouldThrow_NoProductFound_NoProductWithThatID() {

        try {
            cartService.addCartProduct("user1", CART_ID, "20873897-0iier0-0wei");
            Assertions.fail("Should throw an exception: Product not found ");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessage("Product not found");
        }
    }

    @Test
    @DisplayName("Try to add product to cart however cart not found")
    public void addProduct_shouldThrow_CartNotFound_TryToAddProductToNotFoundCart() {
        try {
            cartService.addCartProduct("user1", "20873897-0iier0-0wei", PRODUCT_REPOSITORY_ID);
            Assertions.fail("Should throw an exception: Cart not found ");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(CartNotFoundException.class)
                    .hasMessage("Cart not Found");
        }
    }

    @Test
    @DisplayName("Try to add product however the product already added")
    public void addProduct_shouldThrow_ProductAlreadyAdded_TryToExistingProduct() {
        try {
            cartService.addCartProduct("user1", CART_ID, PRODUCT_CART_2_ID);
            Assertions.fail("Should throw an exception: The product already added");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(ProductAlreadyAddedException.class)
                    .hasMessage("The product already added before");
        }
    }


    @Test
    @DisplayName("remove product to existing cart")
    public void removeProduct_shouldRemoveProduct() throws CartNotFoundException, ProductNotFoundException {
        Product p = new Product();
        p.setId(UUID.fromString(PRODUCT_CART_1_ID));
        Assertions.assertThat(cartService.removeCartProduct("user1", CART_ID, PRODUCT_CART_1_ID).getProducts()).doesNotContain(p);
    }

    @Test
    @DisplayName("Try to remove product is not one being added to cart")
    public void removeProduct_shouldThrow_NoProductFound_NoProductWithThatIDAddedToCart() {

        try {
            cartService.removeCartProduct("user1", CART_ID, PRODUCT_REPOSITORY_ID);
            Assertions.fail("Should throw an exception: Product not found ");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessage("Product not found");
        }
    }

    @Test
    @DisplayName("Try to remove product from cart however cart not found")
    public void removeProduct_shouldThrow_CartNotFound_TryToRemoveProductWhileCartNotFound() {
        try {
            cartService.addCartProduct("user1", "20873897-0iier0-0wei", PRODUCT_CART_1_ID);
            Assertions.fail("Should throw an exception: Cart not found ");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(CartNotFoundException.class)
                    .hasMessage("Cart not Found");
        }
    }

    @Test
    @DisplayName("Try to remove product is not one of the available products")
    public void removeProduct_shouldThrow_NoProductFound_NoProductWithThatID() {

        try {
            cartService.removeCartProduct("user1", CART_ID, "8400000-8cf0-11bd-b23e-6666699");
            Assertions.fail("Should throw an exception: Product not found ");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessage("Product not found");
        }
    }


    @Test
    @DisplayName("Try to remove product with invalid uuid format")
    public void removeProduct_shouldThrow_NumberFormatException_InvalidUUID() {

        try {
            cartService.removeCartProduct("user1", CART_ID, "8400000-8cf0-11bd-b23e-errtre");
            Assertions.fail("Should throw an exception: NumberFormatException");
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(NumberFormatException.class);
        }
    }

}
