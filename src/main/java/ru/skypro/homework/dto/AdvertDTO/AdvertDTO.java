package ru.skypro.homework.dto.AdvertDTO;

import ru.skypro.homework.dto.CommentDTO.CommentDTO;

import java.time.LocalDateTime;
import java.util.List;
/**
 * DTO для представления объявления
 */
public class AdvertDTO {
    private Long id; // ID объявления
    private String title; // Заголовок объявления
    private String description; // Подробное описание
    private Double price; // Цена товара
    private String[] images; // Массив URL изображений товара
    private UserShortDTO author; // Краткая информация об авторе
    private List<CommentDTO> comments; // Список комментариев
    private LocalDateTime createdAt; // Дата создания объявления
}
