package bienew.blog.backend.auth.dto;

import bienew.blog.backend.auth.domain.LoginType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank String credential,
        @NotNull LoginType loginType
) {
}