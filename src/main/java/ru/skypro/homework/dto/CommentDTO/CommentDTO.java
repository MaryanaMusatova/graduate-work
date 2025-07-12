package ru.skypro.homework.dto.CommentDTO;

import ru.skypro.homework.dto.AdvertDTO.UserShortDTO;

import java.time.LocalDateTime;
/**
 * DTO для представления комментария
 */
public class CommentDTO {
    private Long id; // ID комментария
    private String text; // Текст комментария
    private UserShortDTO author; // Автор комментария
    private LocalDateTime createdAt; // Дата создания
}
