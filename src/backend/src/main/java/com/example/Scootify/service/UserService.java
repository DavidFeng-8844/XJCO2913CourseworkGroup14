package com.example.Scootify.service;

import com.example.Scootify.model.User;
import com.example.Scootify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void register(User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists"); // 用户名已存在
        }
        
        // Check if email is already registered
        if (userRepository.findByEmail(user.getEmail()) != null) { // Use findByEmail here
            throw new RuntimeException("Email already registered"); // 邮箱已注册
        }
    
        System.out.println("Plain Password: " + user.getPassword()); // 注册时的明文
        System.out.println("Encoded Password: " + passwordEncoder.encode(user.getPassword())); // 加密后
        System.out.println("Email: " + user.getEmail()); // 注册时的邮箱
    
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt the password
        userRepository.save(user); // Save the user
    }

    public class LoginResponse {
        private String message;
        private User user;
    
        public LoginResponse(String message, User user) {
            this.message = message;
            this.user = user;
        }
    
        // getters and setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
    
    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return new LoginResponse("Login successful", user);
        }
        return new LoginResponse("Invalid username or password", null); // 返回相应的失败提示
    }
}