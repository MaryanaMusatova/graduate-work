package ru.skypro.homework.dto;

public enum Role {
    USER,
    ADMIN;

    public static Role fromString(String value) {
        if (value == null) return USER;

        String normalized = value.trim().toUpperCase();
        switch (normalized) {
            case "ПОЛЬЗОВАТЕЛЬ":
            case "USER":
                return USER;
            case "АДМИНИСТРАТОР":
            case "ADMIN":
                return ADMIN;
            default:
                throw new IllegalArgumentException("Unknown role: " + value);
        }
    }
}