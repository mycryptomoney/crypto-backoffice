package com.alex.cryptoBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/securedHello")
public class TestSecuredController {
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return new ResponseEntity<>("I am visible only for auth users", HttpStatus.OK);
    }
}
