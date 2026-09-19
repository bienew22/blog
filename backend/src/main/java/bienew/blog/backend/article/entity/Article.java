package bienew.blog.backend.article.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "articles")
public class Article {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "slug", nullable = false)
    String slug;

    @Column(name = "content_md", nullable = false)
    String contentMd;

    @Column(name = "content_html", nullable = false)
    String contentHtml;

    @Column(name = "author", nullable = false)
    String author;

    @Column(name = "create_at", nullable = false)
    LocalDateTime createAt;

    @Column(name = "update_at", nullable = false)
    LocalDateTime updateAt;
}
