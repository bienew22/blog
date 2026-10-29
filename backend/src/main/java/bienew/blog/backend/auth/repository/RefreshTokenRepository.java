package bienew.blog.backend.auth.repository;

import bienew.blog.backend.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("select t from RefreshToken t join fetch t.user where t.tokenHash = :tokenHash")
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    // 동시 요청 시 한 번만 성공하도록 삭제된 행 수로 판단한다.
    @Modifying
    @Query("delete from RefreshToken t where t.tokenHash = :tokenHash")
    int deleteByTokenHash(String tokenHash);

    @Modifying
    @Query("delete from RefreshToken t where t.expiresAt < :now")
    void deleteExpired(LocalDateTime now);
}
