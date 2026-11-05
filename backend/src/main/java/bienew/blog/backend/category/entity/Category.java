package bienew.blog.backend.category.entity;


import bienew.blog.backend.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;

    @Column(name = "category_desc")
    private String categoryDesc;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private final List<Post> posts = new ArrayList<>();

    public static Category create(Long id, String categoryName, String categoryDesc) {
        Category category = new Category();
        category.id = id;
        category.categoryName = categoryName;
        category.categoryDesc = categoryDesc;
        category.createAt = LocalDateTime.now();
        return category;
    }
}
