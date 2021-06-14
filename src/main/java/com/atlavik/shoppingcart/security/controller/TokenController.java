package com.atlavik.shoppingcart.security.controller;

import com.atlavik.shoppingcart.security.service.UserService;
import com.atlavik.shoppingcart.security.utils.BasicAuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(authorizations = {@Authorization(value = "basicAuth")})
@RestController
@RequestMapping(value = "v1/api")
public class TokenController {

    @Autowired
    private BasicAuthUtil basicAuthUtil;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/auth",produces = "text/plain")
    public ResponseEntity generateToken(@RequestHeader(name = "Authorization") String authorization){
      String[] usernameAndPassword =  basicAuthUtil.getUserNameAndPasswordFromBasicAuth(authorization);
        String token = userService.login(usernameAndPassword[0], usernameAndPassword[1]);
        return ResponseEntity.ok(token);
    }
}
