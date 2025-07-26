package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.repository.ImageRepository;

import java.time.Duration;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));

        if (image.getData() == null || image.getData().length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image data is empty");
        }

        MediaType mediaType;
        try {
            mediaType = Optional.ofNullable(image.getMediaType())
                    .map(MediaType::parseMediaType)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentLength(image.getData().length);
        headers.setCacheControl(CacheControl.maxAge(Duration.ofDays(30)).getHeaderValue());
        headers.set("Content-Disposition", "inline; filename=\"image\"");

        return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
    }
}