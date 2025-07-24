package ru.skypro.homework.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUser {
    private String firstName;
    private String lastName;

    private String phone;
}