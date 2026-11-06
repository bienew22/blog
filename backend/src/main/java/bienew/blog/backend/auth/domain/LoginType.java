package bienew.blog.backend.auth.domain;

import bienew.blog.backend.post.domain.PostStatus;

public enum LoginType {
    GOOGLE;

    public static LoginType fromString(String status) {
        for (LoginType loginType : LoginType.values()) {
            if (loginType.name().equalsIgnoreCase(status)) {
                return loginType;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
