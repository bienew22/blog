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
    Long id;

    @Column(name = "tag_name", nullable = false)
    private String tagName;

    @Column(name = "tag_desc")
    private String tagDesc;

    @Column(name = "post_cnt", nullable = false, columnDefinition = "int default 0")
    private Integer postCnt = 0;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> postTags = new HashSet<>();

    @Column(name = "create_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    public static Tag create(Long id, String tagName, String tagDesc) {
        Tag tag = new Tag();
        tag.id = id;
        tag.tagName = tagName;
        tag.tagDesc = tagDesc;
        return tag;
    }

    public void incrementPostCount() {
        this.postCnt++;
    }

}
