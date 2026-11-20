package bienew.blog.backend.image.entity;

import bienew.blog.backend.mapping.entity.PostImage;
import bienew.blog.backend.mapping.entity.PostTag;
import bienew.blog.backend.post.entity.Post;
import bienew.blog.backend.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<PostImage> postImages = new HashSet<>();

    public static Image image(String imageType, Long imageSize) {
        Image image = new Image();

        image.imageType = imageType;
        image.imageSize = imageSize;

        return image;
    }

    public void removePost(Long postId) {
        postImages.removeIf(postImage -> postImage.getPost().getId().equals(postId));
    }

    public void addPost(Post post) {
        postImages.add(PostImage.create(post, this));
    }
}
