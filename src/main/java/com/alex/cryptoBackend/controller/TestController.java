package com.alex.cryptoBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/hello")
public class TestController {
    String [] randomWords = {"Za sebea i za sashku", "Jopa ne noga", "Sisea ne pisea", "Ia siel deda", "Ne problema", "Govno tebe v nos", "U erjana ponos", "Ia kiska", "Liubiteli tselovati pisi"};
    @GetMapping
    public ResponseEntity<String> sayHello() {
        Random random = new Random();
        int index = random.nextInt(randomWords.length);
        String word = randomWords[index];
        return new ResponseEntity<>(word, HttpStatus.OK);
    }
}
