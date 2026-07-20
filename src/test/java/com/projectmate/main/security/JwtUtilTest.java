package com.projectmate.main.security;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void shouldValidateTokenSignedWithHs384() {
        ReflectionTestUtils.setField(jwtUtil, "secret", "ProjectMateSuperSecretKeyForJwtAuthentication2026ProjectMate");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);

        String token = Jwts.builder()
                .setSubject("john@example.com")
                .signWith(Keys.hmacShaKeyFor("ProjectMateSuperSecretKeyForJwtAuthentication2026ProjectMate".getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS384)
                .compact();

        assertThat(jwtUtil.validateToken(token)).isTrue();
        assertThat(jwtUtil.extractUsername(token)).isEqualTo("john@example.com");
    }
}
