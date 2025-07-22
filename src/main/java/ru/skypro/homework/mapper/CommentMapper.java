package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Image;

import java.time.ZoneId;

@Mapper(componentModel = "spring", imports = ZoneId.class)
public interface CommentMapper {

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.image", target = "authorImage")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(target = "createdAt",
            expression = "java(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())")
    @Mapping(source = "text", target = "text")
    CommentDTO commentEntityToCommentDTO(Comment comment);

    default String mapImageToString(Image image) {
        if (image == null) {
            return null;
        }
        return "/images/" + image.getId();
    }
}