package com.ganoninc.viteurlshortener.authservice.repository;

import com.ganoninc.viteurlshortener.authservice.model.RefreshTokenMapping;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenMapping, UUID> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select r from RefreshTokenMapping r where r.id = :id")
  Optional<RefreshTokenMapping> findByIdForUpdate(@Param("id") UUID id);
}
