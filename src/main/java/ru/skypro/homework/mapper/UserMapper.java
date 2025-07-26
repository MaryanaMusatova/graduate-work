package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.Users;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "username", target = "email")
    @Mapping(target = "role", defaultValue = "USER")
    Users registerToUser(Register register);

    @Mapping(source = "email", target = "email")
    @Mapping(target = "image", expression = "java(user.getImage() != null ? \"/images/\" + user.getImage().getId() : null)")
    User userEntityToUserDTO(Users user);
}