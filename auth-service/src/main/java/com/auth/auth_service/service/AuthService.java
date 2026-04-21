package com.auth.auth_service.service;

import com.auth.auth_service.entity.User;
import com.auth.auth_service.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;

    public AuthService(UserRepository repo) {
        this.repo = repo;
    }

    public User register(User user) {
        return repo.save(user);
    }

    public String login(String username, String password) {
        User user = repo.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return "Login Successful";
        }
        return "Invalid Credentials";
    }
}