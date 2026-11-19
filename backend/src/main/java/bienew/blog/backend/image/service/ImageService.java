package bienew.blog.backend.image.service;

import bienew.blog.backend.image.dto.ImageUploadResponse;
import bienew.blog.backend.image.entity.Image;
import bienew.blog.backend.image.repository.ImageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB


    private static final Map<String, String> ALLOWED_CONTENT_TYPES = Map.of(
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "png", "image/png",
            "gif", "image/gif",
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/gif", "gif",
            "image/jpg", "jpg"
    );

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path uploadPath;

    @PostConstruct
    void init() throws IOException {
        uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        Files.createDirectories(uploadPath);
    }

    @Transactional
    public ImageUploadResponse uploadImage(MultipartFile file) {
        String extension = validateAndGetExtension(file);

        Image image = imageRepository.save(
                Image.image(extension, file.getSize())
        );

        try {
            file.transferTo(uploadPath.resolve(image.getId() + "." + extension));
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        }

        return ImageUploadResponse.from(image);
    }

    private String validateAndGetExtension(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기가 10MB를 초과했습니다.");
        }

        if (file.getOriginalFilename() == null || !file.getOriginalFilename().contains(".")) {
            throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
        }

        String extension = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf('.') + 1)
                .toLowerCase();

        if (!ALLOWED_CONTENT_TYPES.containsKey(extension)) {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다. (jpg, jpeg, png, gif만 허용)");
        }

        if (file.getContentType() == null || !ALLOWED_CONTENT_TYPES.containsKey(file.getContentType())) {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다. (jpg, jpeg, png, gif만 허용)");
        }

        if (!ALLOWED_CONTENT_TYPES.get(extension).equals(file.getContentType())) {
            throw new IllegalArgumentException("파일 확장자와 MIME 타입이 일치하지 않습니다.");
        }

        return extension;
    }
}
