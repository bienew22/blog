package bienew.blog.backend.tag.repository;


import bienew.blog.backend.post.domain.PostStatus;
import bienew.blog.backend.tag.dto.TagSummaryResponse;
import bienew.blog.backend.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String tagName);

    List<Tag> findAllByTagNameIn(List<String> tagNames);

    @Query("""
            SELECT new bienew.blog.backend.tag.dto.TagSummaryResponse(t.tagName, COUNT(p))
            FROM Tag t
            LEFT JOIN t.postTags pt
            LEFT JOIN pt.post p ON p.status IN :statuses
            GROUP BY t.id, t.tagName
            ORDER BY t.tagName
    """)
    List<TagSummaryResponse> findAllWithPostCount(List<PostStatus> statuses);
}
