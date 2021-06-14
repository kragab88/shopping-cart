package com.atlavik.shoppingcart.security.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.function.Function;

public interface TokenService {

    public String getUsernameFromToken(String token);

    public Date getExpirationDateFromToken(String token);

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    public String generateToken(UserDetails userDetails);

    public Boolean isValidToken(String token);

    public String resolveToken(HttpServletRequest req);


}
