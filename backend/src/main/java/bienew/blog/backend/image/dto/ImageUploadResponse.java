package bienew.blog.backend.image.dto;

import bienew.blog.backend.image.entity.Image;

import java.util.UUID;

public record ImageUploadResponse(
        UUID id,
        String url
) {
    public static ImageUploadResponse from(Image image) {
        return new ImageUploadResponse(image.getId(), "/api/v1/images/" + image.getId());
    }
}
