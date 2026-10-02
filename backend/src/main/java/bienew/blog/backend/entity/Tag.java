package bienew.blog.backend.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "tags")
public class Tag {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "tag_name", nullable = false)
    String tagName;

    @Column(name = "tag_desc")
    String tagDesc;

    @Column(name = "post_cnt", nullable = false, columnDefinition = "int default 0")
    Integer postCnt;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> postTags = new HashSet<>();

    @Column(name = "create_at", updatable = false, insertable = false)
    LocalDateTime createdAt;
}
