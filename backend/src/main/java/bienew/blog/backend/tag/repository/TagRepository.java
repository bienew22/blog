package bienew.blog.backend.tag.repository;


import bienew.blog.backend.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("""
            SELECT DISTINCT t
            FROM Tag t
            LEFT JOIN FETCH t.postTags pt
            LEFT JOIN FETCH pt.post
            WHERE t.tagName = :tagName
    """)
    Optional<Tag> findByTagNameWithPosts(String tagName);

    Optional<Tag> findByTagName(String tagName);

    List<Tag> findAllByTagNameIn(List<String> tagNames);

    @Modifying
    @Query("""
            UPDATE Tag t
            SET t.postCnt = t.postCnt + 1
            WHERE t.tagName IN :tagNames
    """)
    void incrementPostCount(List<String> tagNames);
}
