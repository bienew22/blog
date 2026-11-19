package bienew.blog.backend.image.entity;

import bienew.blog.backend.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 10)
    private String imageType;

    @Column(nullable = false)
    private Long imageSize;

    @Column(nullable = false, updatable = false, insertable = false)
    private LocalDateTime createAt;

    public static Image image(String imageType, Long imageSize) {
        Image image = new Image();

        image.imageType = imageType;
        image.imageSize = imageSize;

        return image;
    }
}
