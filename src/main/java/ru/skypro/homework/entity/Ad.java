package ru.skypro.homework.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ads")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id", nullable = false)
    private Integer id;

    @Max(32)
    @Min(4)
    @Column(name = "title", nullable = false, length = 32)
    private String title;

    @Max(10000000)
    @Column(name = "price", nullable = false)
    private Integer price;

    @Max(64)
    @Min(8)
    @Column(name = "description", length = 64)
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Users author;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comments;
}