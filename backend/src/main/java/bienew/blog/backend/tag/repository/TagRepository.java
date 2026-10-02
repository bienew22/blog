package bienew.blog.backend.tag.repository;


import bienew.blog.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
    Optional<Tag> findByName(String tagName);
}
