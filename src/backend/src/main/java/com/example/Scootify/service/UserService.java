package com.example.Scootify.service;

import com.example.Scootify.model.User;
import com.example.Scootify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 从配置文件读取密钥
    @Value("${admin.secret.key}")
    private String adminSecretKey;

    public void register(User user, String secretKey) {
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

        // 检查密钥并设置角色
        if (secretKey != null && adminSecretKey.equals(secretKey)) {
            user.getRoles().add("ROLE_ADMIN");
        } else if (secretKey != null && !adminSecretKey.equals(secretKey)) {
            user.getRoles().add("ROLE_USER"); // 默认普通用户角色
        }// 否则报错
        else {
            throw new RuntimeException("Invalid secret key for registration"); // 密钥无效
        }
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

    // public void registerAdmin(String username, String password, String secretKey) {
    //     // 验证提供的密钥
    //     if (!adminSecretKey.equals(secretKey)) {
    //         throw new RuntimeException("Invalid secret key for admin registration");
    //     }

    //     User adminUser = new User();
    //     adminUser.setUsername(username);
    //     adminUser.setPassword(passwordEncoder.encode(password)); // 加密密码
    //     adminUser.getRoles().add("ROLE_ADMIN"); // 设置管理员角色

    //     userRepository.save(adminUser); // 保存管理员用户
    // }
    
    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return new LoginResponse("Login successful", user);
        }
        return new LoginResponse("Invalid username or password", null); // 返回相应的失败提示
    }

    public void updateUser(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getId())
            .orElseThrow(() -> new RuntimeException("用户未找到"));

        // 更新用户信息（如果需要）
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setUsername(updatedUser.getUsername());
        // 如果提供了新的密码，更新密码
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(existingUser); // 保存更新后的用户信息
    }
}