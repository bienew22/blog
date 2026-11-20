package bienew.blog.backend.image.repository;

import bienew.blog.backend.image.entity.Image;
import bienew.blog.backend.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    List<Image> findAllByIdIn(List<UUID> imageUrls);

    @Query("""
        SELECT i
        FROM Image i
        JOIN i.postImages pi
        WHERE pi.post.id = :postId
    """)
    List<Image> findAllByPostId(Long postId);
}
