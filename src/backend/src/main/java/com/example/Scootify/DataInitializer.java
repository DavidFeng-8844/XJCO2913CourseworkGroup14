package com.example.Scootify;

import com.example.Scootify.model.User;
import com.example.Scootify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        // 创建管理员用户
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // 默认密码
            admin.setEmail("admin@admin.com");
            admin.getRoles().add("ROLE_ADMIN"); // 设置角色
            userRepository.save(admin);
        }

        // 创建其他角色的用户等...
        if (userRepository.findByUsername("user") == null) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123")); // 默认密码
            user.setEmail("user@user.com");
            user.getRoles().add("ROLE_USER"); // 设置角色
            userRepository.save(user);
        }
    }
}