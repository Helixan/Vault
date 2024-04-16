package org.cultro.vault.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.cultro.vault.service.UserService;
import org.cultro.vault.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            userService.registerUser(request.getUsername(), request.getPassword(), request.getEmail());
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequest request) {
        boolean valid = userService.checkPassword(request.getUsername(), request.getPassword());
        if (valid) {
            String token = jwtUtils.generateJwtToken(request.getUsername());
            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @Getter
    @Setter
    public static class UserRegistrationRequest {
        private String username;
        private String password;
        private String email;
    }

    @Getter
    @Setter
    public static class UserLoginRequest {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class JwtResponse {
        private String token;
        private String type = "Bearer";

        public JwtResponse(String token) {
            this.token = token;
        }
    }
}
