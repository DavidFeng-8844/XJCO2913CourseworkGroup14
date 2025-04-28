package com.example.Scootify.controller;

import com.example.Scootify.model.User;
import com.example.Scootify.service.UserService;
import com.example.Scootify.service.UserService.LoginResponse;
import com.example.Scootify.dto.UserDTO; // 导入新创建的UserDTO类
import com.example.Scootify.dto.LoginRequest; // 导入新创建的LoginRequest类
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        try {
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setEmail(userDTO.getEmail()); // 设置邮箱
            userService.register(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage()); // 返回异常消息给前端
        }
    }

    @PostMapping("/login")
    // public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
    //     User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    //     if (user != null) {
    //         return ResponseEntity.ok("Login successful");
    //     } else {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Auth: Login failed");
    //     }
    // }
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (response.getUser() != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 返回401未授权状态
    }

}