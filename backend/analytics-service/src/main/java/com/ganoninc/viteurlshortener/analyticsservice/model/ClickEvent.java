package com.ganoninc.viteurlshortener.analyticsservice.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(indexes = {@Index(columnList = "shortId, country")})
public class ClickEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String shortId;
  private Instant timestamp;
  private String ip;
  private String userAgent;

  @Column(length = 2)
  private String country;
}
