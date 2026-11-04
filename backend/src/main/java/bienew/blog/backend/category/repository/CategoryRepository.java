package bienew.blog.backend.category.repository;

import bienew.blog.backend.category.dto.CategorySummaryResponse;
import bienew.blog.backend.category.entity.Category;
import bienew.blog.backend.post.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryName(String s);

    @Query("""
            SELECT new bienew.blog.backend.category.dto.CategorySummaryResponse(c.categoryName, c.categoryDesc, COUNT(p), MAX(p.createAt))
            FROM Category c
            LEFT JOIN c.posts p ON p.status IN :statuses
            GROUP BY c.id, c.categoryName, c.categoryDesc
            ORDER BY count(p) DESC, c.id ASC
    """)
    List<CategorySummaryResponse> findAllCategorySummary(List<PostStatus> statuses);
}
