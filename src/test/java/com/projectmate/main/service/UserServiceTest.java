package com.projectmate.main.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projectmate.main.dto.RegisterRequest;

@SpringBootTest(properties = "spring.config.location=classpath:/application-test.properties")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldRegisterUserWithEncodedPassword() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test.user@example.com");
        request.setPassword("password123");

        var response = userService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("test.user@example.com");
        assertThat(response.getPassword()).isNotEqualTo("password123");
    }
}
