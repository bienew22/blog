package bienew.blog.backend.post.dto;

import bienew.blog.backend.category.dto.CategoryResponse;
import bienew.blog.backend.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
        String id,
		String title,
		String slug,
		LocalDateTime createAt,
		String content,
		String contentMd,
		CategoryResponse category,
		List<String> tags
) {
	public static PostDetailResponse of(Post post) {
		return new PostDetailResponse(
				post.getId().toString(),
				post.getTitle(),
				post.getSlug(),
				post.getCreateAt(),
				post.getContentHtml(),
				post.getContentMd(),
				CategoryResponse.from(post.getCategory()),
				post.getPostTags().stream()
						.map(postTag -> postTag.getTag().getTagName())
						.toList()
		);
	}

}
