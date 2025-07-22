package ru.skypro.homework.service;

import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.UpdateUser;

public interface UserService {
    User getCurrentUserInfo();
    User updateUserInfo(UpdateUser updateUser);
    void setUserImage(String imageUrl); // Принимает URL/путь к изображению
    boolean registerUser(ru.skypro.homework.dto.Register registerForm);
}