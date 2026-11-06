package bienew.blog.backend.auth.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Validated
@ConfigurationProperties(prefix = "app.auth")
public record AuthProperties(
        @DefaultValue("15m") @DurationUnit(ChronoUnit.MINUTES) Duration accessTokenLifetime,
        @DefaultValue("14d") @DurationUnit(ChronoUnit.DAYS) Duration refreshTokenLifetime
) {
}