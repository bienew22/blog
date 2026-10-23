package bienew.blog.backend.post.entity;


import bienew.blog.backend.post.domain.PostStatus;
import bienew.blog.backend.tag.entity.Tag;
import bienew.blog.backend.mapping.entity.PostTag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
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
    private final Set<PostTag> postTags = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PostStatus status = PostStatus.DRAFT;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    public static Post create(
            Long id, String title, String slug, String contentMd, String contentHtml, String author, List<Tag> tags, PostStatus status) {
        LocalDateTime now = LocalDateTime.now();

        Post post = new Post();
        post.id = id;
        post.title = title;
        post.slug = slug;

        post.contentMd = contentMd;
        post.contentHtml = contentHtml;

        post.author = author;

        post.status = status;

        post.createAt = now;
        post.updateAt = now;

        for (Tag tag : tags) {
            PostTag postTag = PostTag.create(post, tag);
            post.postTags.add(postTag);
        }

        return post;
    }

    public void update(String title, String contentMd, String contentHtml, PostStatus status) {
        this.title = title;
        this.contentMd = contentMd;
        this.contentHtml = contentHtml;
        this.status = status;
        this.updateAt = LocalDateTime.now();
    }

    public void removeTags(Collection<String> tagNames) {
        postTags.removeIf(postTag -> tagNames.contains(postTag.getTag().getTagName()));
    }

    public void addTags(Collection<Tag> tags) {
        for (Tag tag : tags) {
            postTags.add(PostTag.create(this, tag));
        }
    }
}
