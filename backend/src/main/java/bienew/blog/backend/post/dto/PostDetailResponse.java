package bienew.blog.backend.post.dto;

import bienew.blog.backend.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
		String title,
		String slug,
		LocalDateTime createAt,
		String content,
		List<String> tags
) {
	public static PostDetailResponse of(Post post) {
		return new PostDetailResponse(
				post.getTitle(),
				post.getSlug(),
				post.getCreateAt(),
				post.getContentHtml(),
				post.getPostTags().stream()
						.map(postTag -> postTag.getTag().getTagName())
						.toList()
		);
	}

}
