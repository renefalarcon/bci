package cl.bci.users.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void shouldGenerateJwtToken() {

        String secret = "bci-super-secure-jwt-secret-key-256-bits!!";
        long expiration = 3600000;

        JwtService jwtService = new JwtService(secret, expiration);

        String token = jwtService.generateToken("test@test.cl");

        assertNotNull(token);
        assertTrue(token.startsWith("ey"));
    }

}