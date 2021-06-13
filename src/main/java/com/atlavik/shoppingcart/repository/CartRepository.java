package com.atlavik.shoppingcart.repository;

import com.atlavik.shoppingcart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    public Optional<Cart> findByUsername(String username);

    public Optional<Cart> findByIdAndUsername(String id, String username);

    public boolean existsByUsername(String username);

}
