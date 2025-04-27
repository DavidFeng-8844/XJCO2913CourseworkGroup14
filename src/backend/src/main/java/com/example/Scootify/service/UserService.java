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
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        System.out.println("Plain Password: " + user.getPassword()); // 注册时的明文
        System.out.println("Encoded Password: " + passwordEncoder.encode(user.getPassword())); // 加密后
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Save email 
        user.setEmail(user.getEmail()); // 如果你在注册时需要邮箱
        userRepository.save(user);
    }
    
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) { // 验证密码
            System.out.println("Plain Password: " + user.getPassword()); // 注册时的明文
            System.out.println("Encoded Password: " + passwordEncoder.encode(user.getPassword())); // 加密后
            return user;
        }
        return null; // 登录失败
    }
}