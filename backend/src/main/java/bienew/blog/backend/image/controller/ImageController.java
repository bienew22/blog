package bienew.blog.backend.image.controller;


import bienew.blog.backend.image.dto.ImageResponse;
import bienew.blog.backend.image.service.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Tag(name = "이미지 관리", description = "이미지 관련 API")
public class ImageController {
    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestPart(name = "file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.uploadImage(file));
    }


    @GetMapping("/{imageId}")
    public ResponseEntity<Resource> getImage(@PathVariable UUID imageId) {
        ImageResponse response = imageService.getImage(imageId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(response.contentType()))
                .body(response.resource());
    }
}
