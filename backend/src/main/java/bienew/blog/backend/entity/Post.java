package bienew.blog.backend.entity;


import bienew.blog.backend.post.dto.PostCreateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
public class Post {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "content_md", nullable = false)
    private String contentMd;

    @Column(name = "content_html", nullable = false)
    private String contentHtml;

    @Column(name = "author", nullable = false)
    private String author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> postTags = new HashSet<>();

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    public static Post create(
            Long id, String title, String slug, String contentMd, String contentHtml, String author, List<Tag> tags) {
        LocalDateTime now = LocalDateTime.now();

        Post post = new Post();
        post.id = id;
        post.title = title;
        post.slug = slug;

        post.contentMd = contentMd;
        post.contentHtml = contentHtml;

        post.author = author;

        post.createAt = now;
        post.updateAt = now;

        for (Tag tag : tags) {
            PostTag postTag = PostTag.create(post, tag);
            post.postTags.add(postTag);
        }

        return post;
    }
}
