package ru.skypro.homework.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.AuthService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Override
    public boolean authenticate(String username, String password) {
        log.info("Attempting authentication for user: {}", username);
        try {
            Optional<Users> user = usersRepository.findByEmail(username);
            if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
                log.info("Authentication successful for user: {}", username);
                return true;
            }
        } catch (UsernameNotFoundException ex) {
            log.error("Authentication error for user: {}", username, ex);
        }
        log.warn("Authentication failed for user: {}", username);
        return false;
    }

    @Override
    public boolean register(Register register) {
        log.info("Registration attempt for user: {}", register.getUsername());

        if (usersRepository.existsByEmail(register.getUsername())) {
            log.warn("Registration failed - user already exists: {}", register.getUsername());
            return false;
        }

        try {
            Users user = new Users();
            user.setEmail(register.getUsername());
            user.setPassword(passwordEncoder.encode(register.getPassword()));
            user.setFirstName(register.getFirstName());
            user.setLastName(register.getLastName());
            user.setPhone(register.getPhone());
            user.setRole(register.getRole());

            usersRepository.save(user);
            log.info("User registered successfully: {}", register.getUsername());
            return true;
        } catch (Exception e) {
            log.error("Registration error for user: {}", register.getUsername(), e);
            return false;
        }
    }

    @Override
    public String generateToken(Users user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    @Override
    public Authentication getAuthentication(String token) {
        // Реализация зависит от  аутентификационной логики
        throw new UnsupportedOperationException("Not implemented yet");
    }
}