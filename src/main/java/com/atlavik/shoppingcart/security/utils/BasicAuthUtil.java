package com.atlavik.shoppingcart.security.utils;

import com.atlavik.shoppingcart.security.exception.InvalidAuthHeaderException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class BasicAuthUtil {


    public String[] getUserNameAndPasswordFromBasicAuth(String authorization) {
        String[] values = null;
        try {
            if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
                // Authorization: Basic base64credentials
                String base64Credentials = authorization.substring("Basic".length()).trim();
                byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
                String credentials = new String(credDecoded, StandardCharsets.UTF_8);
                // credentials = username:password
                values = credentials.split(":", 2);
            }
        } catch (Exception e) {
            throw new InvalidAuthHeaderException(e);
        }
        return values;
    }


}
