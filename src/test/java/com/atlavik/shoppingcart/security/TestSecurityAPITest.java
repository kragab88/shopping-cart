package com.atlavik.shoppingcart.security;

import com.atlavik.shoppingcart.security.enitity.User;
import com.atlavik.shoppingcart.security.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestSecurityAPITest {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;

    @LocalServerPort
    private int port;

    private String url;

    @Autowired
    private TestRestTemplate restTemplate;
    @BeforeAll
    public   void setUpUserRepository() {
        User user1 = new User();
        user1.setActive(true);
        user1.setPassword(passwordEncoder.encode("password"));
        user1.setUsername("user1");

        User user2 = new User();
        user2.setActive(true);
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setUsername("user1");

        Mockito.when(userRepository.findByUsername("user1")).thenReturn(java.util.Optional.of(user1));
        Mockito.when(userRepository.findByUsername("user2")).thenReturn(java.util.Optional.of(user2));


    }

    @BeforeAll
    public void setupTokenApiURL() {
        url = String.format("http://localhost:%d/v1/api/auth", port);
    }

    @Test
    @DisplayName("Login with valid username and password")
    public void tokenAPI_shouldReturnValidJWTToken() {

        ResponseEntity<String> response = restTemplate.withBasicAuth(
                "user1", "password").getForEntity(url,
                String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotBlank();

    }


    @Test
    @DisplayName("Login with invalid password")
    public void tokenAPI_shouldReturnInvalidPassword() {

        ResponseEntity<String> response = restTemplate.withBasicAuth(
                "user1", "wrongPassword").getForEntity(url,
                String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
        Assertions.assertThat(response.getBody()).contains("Invalid username/password");

    }

    @Test
    @DisplayName("Login with invalid username")
    public void tokenAPI_shouldReturnInvalidUserName() {

        ResponseEntity<String> response = restTemplate.withBasicAuth(
                "user444", "password").getForEntity(url,
                String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
        Assertions.assertThat(response.getBody()).contains("Invalid username/password");

    }
}
