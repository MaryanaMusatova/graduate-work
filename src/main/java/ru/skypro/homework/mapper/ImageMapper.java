package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.skypro.homework.entity.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Named("imageToPath") // Добавляем аннотацию @Named
    default String convertImageToPath(Image image) {
        if (image == null) {
            return null;
        }
        return "/images/" + image.getId();
    }
}