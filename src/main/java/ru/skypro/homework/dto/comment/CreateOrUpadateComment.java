package ru.skypro.homework.dto.comment;

import lombok.Data;

@Data
public class CreateOrUpadateComment {

    //      @Size(min = 8, max = 64)
    private String text;
}
