package com.example.Scootify.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "\"user\"") // 使用双引号避免SQL关键字冲突
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;
    // 使用 List 等类型避免过于宽泛
    private Collection<? extends GrantedAuthority> authorities;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // 返回用户的权限
    }

    // 实现 UserDetails 接口所需的方法
    @Override
    public boolean isAccountNonExpired() {
        return true; // 默认用户账户未过期
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 默认用户账户未被锁定
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 默认用户凭证未过期
    }

    @Override
    public boolean isEnabled() {
        return true; // 默认用户已启用
    }
}