package com.ganoninc.viteurlshortener.authservice.controllers;

import com.ganoninc.viteurlshortener.authservice.config.AppProperties;
import com.ganoninc.viteurlshortener.authservice.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private JwtUtils jwtUtils;
    @MockitoBean
    private AppProperties appProperties;


    @Test
    void shouldReturnOauthCallbackViewWithModelAttributes() throws Exception {
        String email = "test@gmail.com";
        String token = "jwt-token";
        String frontendOrigin = "http://localhost:8080";

        when(jwtUtils.generateToken(email)).thenReturn(token);
        when(appProperties.getFrontendOrigin()).thenReturn(frontendOrigin);

        OAuth2User oAuth2User = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of("email", email),
                "email"
        );

        mvc.perform(MockMvcRequestBuilders.get("/oauth-callback").with(oauth2Login().oauth2User(oAuth2User)))
                .andExpect(status().isOk())
                .andExpect(view().name("oauth-callback"))
                .andExpect(model().attribute("token", token))
                .andExpect(model().attribute("email", email))
                .andExpect(model().attribute("frontendOrigin", frontendOrigin));

        verify(jwtUtils).generateToken(email);
        verify(appProperties).getFrontendOrigin();
    }
}
