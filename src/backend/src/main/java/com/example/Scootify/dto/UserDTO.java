package com.example.Scootify.dto;

import java.util.stream.Collectors;
import java.util.List;
import com.example.Scootify.model.User; // Adjust the package path if necessary


import org.springframework.security.core.GrantedAuthority;

public class UserDTO {
    private String username;
    private String password;
    private String email; // 如果你在注册时需要邮箱
    private List<String> authorities;

    // Constructors
    public UserDTO() {}

    // Only accept user object for authorities
    public UserDTO(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword(); // 这里可以选择不返回密码
        this.email = user.getEmail(); // 如果你在注册时需要邮箱
        this.authorities = user.getAuthorities()
                               .stream()
                               .map(GrantedAuthority::getAuthority)
                               .collect(Collectors.toList());
    }

    public UserDTO(String username, String password, String email, User user) {
        this.username = username;
        this.password = password;
        this.email = email;
        // Assuming User is a parameter passed to the constructor
        this.authorities = user.getAuthorities()
                               .stream()
                               .map(GrantedAuthority::getAuthority)
                               .collect(Collectors.toList());
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}