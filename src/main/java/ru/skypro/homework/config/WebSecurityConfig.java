package ru.skypro.homework.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.sql.DataSource;
import java.io.IOException;

@Slf4j
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/webjars/**",
            "/api/auth/**",
            "/register",
            "/login",
            "/ads/image/**",
            "/users/image/**",
            "/error"
    };

    private final DataSource dataSource;

    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring security filter chain");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ads").permitAll()
                        .requestMatchers("/ads/**", "/users/**").authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((request, response, authException) -> {
                                    log.warn("Authentication failed: {}", authException.getMessage());
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                                }
                        ));

        return http.build();
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
                "SELECT email as username, password, true as enabled FROM users WHERE email = ?");
        manager.setAuthoritiesByUsernameQuery(
                "SELECT email as username, role FROM users WHERE email = ?");
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Slf4j
    static class InvalidRequestFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws IOException, ServletException {
            try {
                logRequestDetails(request);
                if (isInvalidRequest(request)) {
                    handleInvalidRequest(response, "Invalid HTTP request");
                    return;
                }
                filterChain.doFilter(request, response);
            } catch (IllegalArgumentException e) {
                log.error("Invalid request", e);
                handleInvalidRequest(response, "Invalid character in method name");
            }
        }

        private void logRequestDetails(HttpServletRequest request) {
            log.debug("Request: {} {} {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getProtocol());
        }

        private boolean isInvalidRequest(HttpServletRequest request) {
            String method = request.getMethod();
            boolean invalid = method == null || !method.matches("GET|POST|PUT|DELETE|PATCH|OPTIONS");
            if (invalid) {
                log.warn("Invalid HTTP method detected: {}", method);
            }
            return invalid;
        }

        private void handleInvalidRequest(HttpServletResponse response, String message) throws IOException {
            log.warn("Blocking invalid request: {}", message);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().write(message);
        }
    }
}