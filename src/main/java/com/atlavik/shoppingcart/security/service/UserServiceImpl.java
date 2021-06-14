package com.atlavik.shoppingcart.security.service;

import com.atlavik.shoppingcart.security.exception.InvalidUsernamePasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CartUserDetailsServiceImpl cartUserDetailsService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDetails user = cartUserDetailsService.loadUserByUsername(username);
            return tokenService.generateToken(user);
        } catch (AuthenticationException e) {
            throw new InvalidUsernamePasswordException(e);
        }
    }


}
