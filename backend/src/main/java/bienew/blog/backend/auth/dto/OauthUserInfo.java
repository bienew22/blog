package bienew.blog.backend.auth.dto;

public record OauthUserInfo(
        String email,
        String name,
        String sub) {
    public static OauthUserInfo of(String email, String name, String sub) {
        return new OauthUserInfo(email, name, sub);
    }
}
