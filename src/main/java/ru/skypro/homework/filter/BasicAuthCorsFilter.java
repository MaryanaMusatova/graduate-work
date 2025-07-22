package ru.skypro.homework.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Добавляем CORS-заголовки
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

        // Обрабатываем OPTIONS запросы для CORS preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Перенаправляем POST /register на /api/auth/register
        if ("/register".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher("/api/auth/register").forward(request, response);
            return;
        }

        // Перенаправляем POST /login на /api/auth/login
        if ("/login".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher("/api/auth/login").forward(request, response);
            return;
        }

        // Пропускаем запрос дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }
}