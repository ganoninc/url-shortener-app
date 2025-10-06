package com.ganoninc.viteurlshortener.authservice.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class RefreshTokenMapping {
  @Id private UUID id;

  @Column(nullable = false)
  private String userEmail;

  @Column(nullable = false, length = 60)
  private String tokenHash;

  private Instant createdAt;
  private Instant expiresAt;
  private Instant revokedAt;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "replacement_id")
  private RefreshTokenMapping replacement;
}
