package com.atlavik.shoppingcart.security.filter;

import com.atlavik.shoppingcart.security.exception.InvalidTokenException;
import com.atlavik.shoppingcart.security.service.CartUserDetailsServiceImpl;
import com.atlavik.shoppingcart.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    @Qualifier("CartUserDetailsServiceImpl")
    private UserDetailsService cartUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenService.resolveToken(httpServletRequest);
            if (token != null && tokenService.isValidToken(token)) {
                String username = tokenService.getUsernameFromToken(token);
                UserDetails userDetails = cartUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken usernamePasswordAuthentication = createUsernamePasswordAuthentication(userDetails, httpServletRequest);

                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthentication);
            }
        } catch (Exception ex) {
            //this is very important, since it guarantees the user is not authenticated at all
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    private UsernamePasswordAuthenticationToken createUsernamePasswordAuthentication(UserDetails userDetails, HttpServletRequest httpServletRequest){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        return  usernamePasswordAuthenticationToken;
    }

}