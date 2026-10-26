package bienew.blog.backend.post.repository;


import bienew.blog.backend.post.domain.PostStatus;
import bienew.blog.backend.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("""
        SELECT DISTINCT p
        FROM Post p
        LEFT JOIN FETCH p.postTags pt
        LEFT JOIN FETCH pt.tag
        WHERE p.status = 'PUBLISHED'
   """)
    List<Post> findAllWithTags();

    @Query("""
        SELECT p
        FROM Post p
        LEFT JOIN FETCH p.postTags pt
        LEFT JOIN FETCH pt.tag
        WHERE p.id = :id
        AND p.status = 'PUBLISHED'
    """)
    Optional<Post> findByIdWithTags(Long id);

    @Query("""
        SELECT p
        FROM Post p
        LEFT JOIN FETCH p.postTags pt
        LEFT JOIN FETCH pt.tag
        WHERE p.id = :id
    """)
    Optional<Post> findByIdForEdit(Long id);

    @Query("""
        SELECT p
        FROM Post p
        JOIN p.postTags pt
        WHERE pt.tag.tagName = :tagName
        AND p.status IN :statuses
    """)
    List<Post> findAllByTagNameAndStatusIn(String tagName, List<PostStatus> statuses);

    boolean existsBySlug(String slug);
}
