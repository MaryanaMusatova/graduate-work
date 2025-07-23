package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.RegisterRequest;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.AuthService;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000/")
public class AuthController {
    private final UsersRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Авторизация пользователя",
            tags = {"Авторизация"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная авторизация"),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные",
                            content = @Content(schema = @Schema(hidden = true))),}
    )
    public ResponseEntity<?> login(@RequestBody Login login) {
        log.info("Attempting login for user: {}", login.getUsername());

        Users user = userRepository.findByEmail(login.getUsername())
                .orElseThrow(() -> {
                    log.warn("User not found: {}", login.getUsername());
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            log.warn("Invalid password for user: {}", login.getUsername());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        String token = authService.generateToken(user);
        log.info("User {} successfully logged in", login.getUsername());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(Map.of(
                        "email", user.getEmail(),
                        "role", user.getRole().name()
                ));
    }

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация нового пользователя",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован"),
                    @ApiResponse(responseCode = "400", description = "Пользователь уже существует"),
                    @ApiResponse(responseCode = "400", description = "Недопустимая роль пользователя")
            }
    )
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        log.info("Registration request for: {}", request.getUsername());

        try {
            Register register = new Register();
            register.setUsername(request.getUsername());
            register.setPassword(request.getPassword());
            register.setFirstName(request.getFirstName());
            register.setLastName(request.getLastName());
            register.setPhone(request.getPhone());

            String normalizedRole = normalizeRole(request.getRole());
            if (normalizedRole == null) {
                log.warn("Invalid role provided: {}", request.getRole());
                return ResponseEntity.badRequest()
                        .body("Недопустимая роль. Используйте: ПОЛЬЗОВАТЕЛЬ или АДМИНИСТРАТОР");
            }
            register.setRole(Role.valueOf(normalizedRole));

            if (userRepository.existsByEmail(register.getUsername())) {
                log.warn("Email already exists: {}", register.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
            }

            Users user = userMapper.registerToUser(register);
            user.setPassword(passwordEncoder.encode(register.getPassword()));
            userRepository.save(user);

            log.info("User registered successfully: {}", register.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            log.error("Registration error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String normalizeRole(String role) {
        if (role == null) return "USER";
        String normalized = role.trim().toUpperCase();
        switch (normalized) {
            case "ПОЛЬЗОВАТЕЛЬ":
            case "USER":
                return "USER";
            case "АДМИНИСТРАТОР":
            case "ADMIN":
                return "ADMIN";
            default:
                return null;
        }
    }
}