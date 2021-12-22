package com.alex.cryptoBackend.controller;

import com.alex.cryptoBackend.dto.*;
import com.alex.cryptoBackend.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiOperation("Authenticate user")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody UserLoginDto loginRequest) {
        JwtResponse jwt = authService.authenticate(loginRequest);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody NewUserDto registerRequest) {
        RegisterResponse registerResponse = authService.register(registerRequest);
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/confirm")
    public ResponseEntity<ConfirmTokenResponse> confirm(@RequestParam("token") String token) {
        final ConfirmTokenResponse confirmTokenResponse = authService.confirmToken(token);
        return ResponseEntity.ok(confirmTokenResponse);
    }

}
