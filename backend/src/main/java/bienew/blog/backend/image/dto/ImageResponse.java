package bienew.blog.backend.image.dto;

import org.springframework.core.io.Resource;

public record ImageResponse(
        String contentType,
        Resource resource
) {
    public static ImageResponse from(String contentType, Resource resource) {
        return new ImageResponse(contentType, resource);
    }
}
