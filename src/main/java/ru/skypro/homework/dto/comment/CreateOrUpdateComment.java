package ru.skypro.homework.dto.comment;


import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class CreateOrUpdateComment {
    private String text;
}