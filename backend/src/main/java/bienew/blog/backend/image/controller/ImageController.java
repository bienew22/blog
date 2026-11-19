package bienew.blog.backend.image.controller;


import bienew.blog.backend.image.service.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
