package bienew.blog.backend.post.repository;


import bienew.blog.backend.entity.Post;
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
   """)
    List<Post> findAllWithTags();

    @Query("""
        SELECT p
        FROM Post p
        LEFT JOIN FETCH p.postTags pt
        LEFT JOIN FETCH pt.tag
        WHERE p.slug = :slug
    """)
	Optional<Post> findBySlug(String slug);
}
