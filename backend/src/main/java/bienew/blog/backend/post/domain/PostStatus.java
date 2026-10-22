package bienew.blog.backend.post.domain;

public enum PostStatus {
    DRAFT("임시저장"),
    PUBLISHED("게시됨");

    private final String description;

    PostStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PostStatus fromString(String status) {
        for (PostStatus postStatus : PostStatus.values()) {
            if (postStatus.name().equalsIgnoreCase(status)) {
                return postStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
