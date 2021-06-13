package com.atlavik.shoppingcart.validator;

import com.atlavik.shoppingcart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
   private CartRepository cartRepository;


    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return !cartRepository.existsByUsername(username);
    }
}