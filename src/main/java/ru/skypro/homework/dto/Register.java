package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Register {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;

    public String getEmail() {
        return username; // username и email - одно и то же в нашей системе
    }

    public void setRoleFromString(String roleValue) {
        this.role = Role.fromString(roleValue);
    }
}