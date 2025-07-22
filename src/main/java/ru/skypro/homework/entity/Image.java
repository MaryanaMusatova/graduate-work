package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer id;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @Column(name = "media_type", length = 50)
    private String mediaType;

    @Column(name = "file_path", length = 255)
    private String filePath;
}