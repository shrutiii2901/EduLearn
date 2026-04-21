package com.auth.auth_service.controller;

import com.auth.auth_service.entity.User;
import com.auth.auth_service.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }

    @GetMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {
        return service.login(username, password);
    }
}