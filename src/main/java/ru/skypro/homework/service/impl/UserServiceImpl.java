package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final ImageRepository imageRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getCurrentUserInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        return userMapper.userEntityToUserDTO(user);
    }

    @Override
    public User updateUserInfo(UpdateUser updateUser) {
        Users user = getCurrentUserEntity();
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());
        return userMapper.userEntityToUserDTO(usersRepository.save(user));
    }

    @Override
    public void setUserImage(String imageUrl) {
        Users user = getCurrentUserEntity();
        if (user.getImage() != null) {
            imageRepository.delete(user.getImage());
        }

        Image newImage = new Image();
        newImage.setFilePath(imageUrl); // предполагая, что в Image есть такое поле
        Image savedImage = imageRepository.save(newImage);

        user.setImage(savedImage);
        usersRepository.save(user);
    }

    @Override
    public boolean registerUser(Register registerForm) {
        if (usersRepository.existsByEmail(registerForm.getUsername())) {
            return false;
        }

        Users newUser = userMapper.registerToUser(registerForm);
        newUser.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        usersRepository.save(newUser);
        return true;
    }

    private Users getCurrentUserEntity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}