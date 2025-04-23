package com.example.Scootify.service;

import com.example.Scootify.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// 假设你还有一个 UserRepository 用于数据存储
import com.example.Scootify.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 密码编码器

    public User register(User user) {
        // 对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        // 实现登录逻辑，例如查找用户并验证密码
        // 这里可以根据你的需求进行实现
        
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user; // 验证通过，返回用户信息
        }
        return null; // 验证失败
    }
}