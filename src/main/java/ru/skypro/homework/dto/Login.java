package ru.skypro.homework.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class Login {

    private String username;
    private String password;
}
