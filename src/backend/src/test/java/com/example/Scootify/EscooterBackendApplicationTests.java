package com.example.Scootify;

import com.example.Scootify.model.User;
import com.example.Scootify.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EscooterBackendApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        // 测试上下文加载
    }
    
    @Test
    void testUserRepository() {
        // 创建一个用户
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("password123");

        // 保存用户
        userRepository.save(user);

        // 查询用户
        User foundUser = userRepository.findByUsername("test_user");
        
        // 验证用户是否正确
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("test_user");
    }
}