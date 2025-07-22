package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.Users;

public interface AuthService {
    /**
     * Метод для авторизации пользователя.
     *
     * @param username Имя пользователя
     * @param password Пароль пользователя
     * @return true если аутентификация успешна
     */
    boolean authenticate(String username, String password);

    /**
     * Метод для регистрации нового пользователя.
     *
     * @param register Форма регистрации пользователя
     * @return true если регистрация успешна
     */
    boolean register(Register register);

    /**
     * Генерация JWT токена для пользователя
     * @param user Аутентифицированный пользователь
     * @return Сгенерированный токен
     */
    String generateToken(Users user);

    /**
     * Получение аутентификации по токену
     * @param token JWT токен
     * @return Объект аутентификации
     */
    Authentication getAuthentication(String token);
}