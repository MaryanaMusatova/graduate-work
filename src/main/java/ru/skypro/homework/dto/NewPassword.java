package ru.skypro.homework.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class NewPassword {
    private String currentPassword;
    private String newPassword;
}