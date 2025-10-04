package com.ganoninc.viteurlshortener.authservice.service.impl;

import com.ganoninc.viteurlshortener.authservice.dto.TokenPairDto;
import com.ganoninc.viteurlshortener.authservice.exception.RefreshTokenInvalidException;
import com.ganoninc.viteurlshortener.authservice.exception.RefreshTokenNotFoundException;
import com.ganoninc.viteurlshortener.authservice.model.RefreshTokenMapping;
import com.ganoninc.viteurlshortener.authservice.repository.RefreshTokenRepository;
import com.ganoninc.viteurlshortener.authservice.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceImplTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private final long refreshTokenDurationMs = 3600000;
    private final String userEmail = "test@example.com";

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationMs", refreshTokenDurationMs);
    }

    @Test
    public void itReturnsARefreshTokenWhenCreatingRefreshToken() {
        String tokenHash = "token-hash";
        when(passwordEncoder.encode(anyString())).thenReturn(tokenHash);

        String result = refreshTokenService.createRefreshToken(userEmail);

        assertThat(result).contains(".");
        String[] parts = result.split("\\.");
        assertThat(parts).hasSize(2);

        ArgumentCaptor<RefreshTokenMapping> captor = ArgumentCaptor.forClass(RefreshTokenMapping.class);
        verify(refreshTokenRepository).save(captor.capture());
        RefreshTokenMapping saved = captor.getValue();

        assertThat(saved.getUserEmail()).isEqualTo(userEmail);
        assertThat(saved.getTokenHash()).isEqualTo(tokenHash);
        assertThat(saved.getId().toString()).isEqualTo(parts[0]);

        Instant now = Instant.now();
        assertThat(saved.getExpiresAt()).isBetween(
                now.plusMillis(refreshTokenDurationMs - 1000),
                now.plusMillis(refreshTokenDurationMs + 1000)
        );
    }

    @Test
    public void itReturnsATokenPairWhenValidateAndRotateTokensIsCalled() {
        UUID currentRefreshTokenId = UUID.randomUUID();
        UUID newRefreshTokenId = UUID.randomUUID();
        String actualCurrentToken = "actualCurrentToken";
        String currentRefreshToken = currentRefreshTokenId + "." + actualCurrentToken;
        String newJwtAccessToken = "newJwtAccessToken";
        String newTokenHash = "newTokenHash";

        RefreshTokenMapping currentRefreshTokenMapping = RefreshTokenMapping.builder()
                .id(currentRefreshTokenId)
                .tokenHash("currentTokenHash")
                .expiresAt(Instant.now().plusMillis(60000))
                .createdAt(Instant.now())
                .userEmail(userEmail)
                .build();

        RefreshTokenMapping newRefreshTokenMapping = RefreshTokenMapping.builder()
                .id(newRefreshTokenId)
                .tokenHash(newTokenHash)
                .expiresAt(Instant.now().plusMillis(60000))
                .createdAt(Instant.now())
                .userEmail(userEmail)
                .build();


        when(refreshTokenRepository.findByIdForUpdate(currentRefreshTokenId)).thenReturn(Optional.of(currentRefreshTokenMapping));
        when(passwordEncoder.matches(actualCurrentToken, currentRefreshTokenMapping.getTokenHash())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn(newTokenHash);
        when(jwtUtils.generateToken(currentRefreshTokenMapping.getUserEmail())).thenReturn(newJwtAccessToken);
        when(refreshTokenRepository.findById(any(UUID.class))).thenReturn(Optional.of(newRefreshTokenMapping));

        TokenPairDto tokenPair = refreshTokenService.validateAndRotateTokens(currentRefreshToken);
        assertThat(tokenPair).isNotNull();
        assertThat(tokenPair.refreshToken()).contains(".");
        assertThat(tokenPair.jwtAccessToken()).isEqualTo(newJwtAccessToken);
    }


    @Test
    public void itThrowsWhenTokenFormatIsInvalid() {
        assertThatThrownBy(() -> refreshTokenService.validateAndRotateTokens("invalid-token"))
                .isInstanceOf(RefreshTokenInvalidException.class)
                .hasMessageContaining("Invalid refresh token");
    }

    @Test
    public void itThrowsWhenTokenNotFound() {
        UUID tokenId = UUID.randomUUID();
        String rawToken = tokenId + ".sometoken";
        when(refreshTokenRepository.findByIdForUpdate(tokenId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshTokenService.validateAndRotateTokens(rawToken))
                .isInstanceOf(RefreshTokenNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    public void itThrowsWhenTokenExpired() {
        UUID tokenId = UUID.randomUUID();
        String rawToken = tokenId + ".actualToken";

        RefreshTokenMapping expired = RefreshTokenMapping.builder()
                .id(tokenId)
                .tokenHash("hash")
                .expiresAt(Instant.now().minusSeconds(60)) // expired
                .createdAt(Instant.now().minusSeconds(120))
                .userEmail(userEmail)
                .build();

        when(refreshTokenRepository.findByIdForUpdate(tokenId)).thenReturn(Optional.of(expired));

        assertThatThrownBy(() -> refreshTokenService.validateAndRotateTokens(rawToken))
                .isInstanceOf(RefreshTokenInvalidException.class)
                .hasMessageContaining("Expired");
    }

    @Test
    public void itThrowsWhenTokenHashDoesNotMatch() {
        UUID tokenId = UUID.randomUUID();
        String rawToken = tokenId + ".actualToken";

        RefreshTokenMapping mapping = RefreshTokenMapping.builder()
                .id(tokenId)
                .tokenHash("differentHash")
                .expiresAt(Instant.now().plusSeconds(60))
                .createdAt(Instant.now())
                .userEmail(userEmail)
                .build();

        when(refreshTokenRepository.findByIdForUpdate(tokenId)).thenReturn(Optional.of(mapping));
        when(passwordEncoder.matches("actualToken", "differentHash")).thenReturn(false);

        assertThatThrownBy(() -> refreshTokenService.validateAndRotateTokens(rawToken))
                .isInstanceOf(RefreshTokenInvalidException.class)
                .hasMessageContaining("Invalid refresh token");
    }

    @Test
    public void itThrowsWhenTokenIsRevoked() {
        UUID tokenId = UUID.randomUUID();
        String rawToken = tokenId + ".actualToken";

        RefreshTokenMapping mapping = RefreshTokenMapping.builder()
                .id(tokenId)
                .tokenHash("hash")
                .expiresAt(Instant.now().plusSeconds(60))
                .createdAt(Instant.now())
                .userEmail(userEmail)
                .revokedAt(Instant.now().minusSeconds(30)) // already revoked
                .build();

        when(refreshTokenRepository.findByIdForUpdate(tokenId)).thenReturn(Optional.of(mapping));
        when(passwordEncoder.matches("actualToken", "hash")).thenReturn(true);

        assertThatThrownBy(() -> refreshTokenService.validateAndRotateTokens(rawToken))
                .isInstanceOf(RefreshTokenInvalidException.class)
                .hasMessageContaining("Revoked");
    }

    @Test
    public void itRevokesEntireChainWhenTokenIsRevoked() {
        UUID tokenId = UUID.randomUUID();
        String rawToken = tokenId + ".actualToken";

        RefreshTokenMapping child = RefreshTokenMapping.builder()
                .id(UUID.randomUUID())
                .userEmail(userEmail)
                .tokenHash("childHash")
                .expiresAt(Instant.now().plusSeconds(120))
                .createdAt(Instant.now())
                .build();

        RefreshTokenMapping parent = RefreshTokenMapping.builder()
                .id(tokenId)
                .userEmail(userEmail)
                .tokenHash("hash")
                .expiresAt(Instant.now().plusSeconds(60))
                .createdAt(Instant.now())
                .revokedAt(Instant.now().minusSeconds(20))
                .replacement(child)
                .build();

        when(refreshTokenRepository.findByIdForUpdate(tokenId)).thenReturn(Optional.of(parent));
        when(passwordEncoder.matches("actualToken", "hash")).thenReturn(true);

        assertThatThrownBy(() -> refreshTokenService.validateAndRotateTokens(rawToken))
                .isInstanceOf(RefreshTokenInvalidException.class)
                .hasMessageContaining("Revoked");

        verify(refreshTokenRepository).save(parent);
        verify(refreshTokenRepository).save(child);
        assertThat(parent.getRevokedAt()).isNotNull();
        assertThat(child.getRevokedAt()).isNotNull();
    }
}
