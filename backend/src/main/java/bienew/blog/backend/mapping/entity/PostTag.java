package bienew.blog.backend.mapping.entity;

import bienew.blog.backend.post.entity.Post;
import bienew.blog.backend.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "post_tags")
@Getter
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public static PostTag create(Post post, Tag tag) {
        PostTag postTag = new PostTag();
        postTag.post = post;
        postTag.tag = tag;
        return postTag;
    }
}
