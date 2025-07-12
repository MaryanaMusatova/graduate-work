package ru.skypro.homework.dto.AdvertDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO для создания/обновления объявления
 */
public class AdvertCreateDTO {
    @NotBlank(message = "Заголовок не может быть пустым")
    private String title; // Заголовок объявления

    @NotBlank(message = "Описание не может быть пустым")
    private String description; // Текст объявления

    @NotNull(message = "Цена должна быть указана")
    @Positive(message = "Цена должна быть положительной")
    private Double price; // Устанавливаемая цена
}
