package com.example.admin.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthInterceptorTest {

    private final JwtService jwtService = mock(JwtService.class);
    private final AuthInterceptor authInterceptor = new AuthInterceptor(jwtService);

    @AfterEach
    void tearDown() {
        CurrentUserContext.clear();
    }

    @Test
    void preHandleShouldRejectRequestWithoutBearerToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(request.getHeader("Authorization")).thenReturn(null);

        boolean result = authInterceptor.preHandle(request, response, new Object());

        assertThat(result).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString()).contains("\"code\":401");
        assertThat(response.getContentAsString()).contains("未登录");
    }

    @Test
    void preHandleShouldSetCurrentUserWhenTokenValid() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token-value");
        when(jwtService.isTokenValid("token-value")).thenReturn(true);
        when(jwtService.getUserId("token-value")).thenReturn(1L);
        when(jwtService.getUsername("token-value")).thenReturn("admin");
        when(jwtService.getPermissions("token-value")).thenReturn(List.of("sys:user:list"));

        boolean result = authInterceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        assertThat(CurrentUserContext.get()).isEqualTo(new CurrentUser(
                1L,
                "admin",
                List.of("sys:user:list")
        ));
    }

    @Test
    void afterCompletionShouldClearCurrentUser() throws Exception {
        CurrentUserContext.set(new CurrentUser(1L, "admin", List.of("sys:user:list")));

        authInterceptor.afterCompletion(
                mock(HttpServletRequest.class),
                mock(HttpServletResponse.class),
                new Object(),
                null
        );

        assertThat(CurrentUserContext.get()).isNull();
    }

}
