package com.example.admin.module.auth;

import com.example.admin.common.BusinessException;
import com.example.admin.module.user.User;
import com.example.admin.module.user.UserMapper;
import com.example.admin.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginShouldReturnTokenAndUserInfoWhenCredentialsValid() {
        User user = adminUser(1);
        List<String> permissions = List.of("sys:user:list", "sys:role:list");
        when(userMapper.findByUsername("admin")).thenReturn(user);
        when(passwordEncoder.matches("123456", "encoded-password")).thenReturn(true);
        when(userMapper.findPermissionsByUserId(1L)).thenReturn(permissions);
        when(jwtService.generateToken(1L, "admin", permissions)).thenReturn("token-value");

        LoginResponse response = authService.login(new LoginRequest("admin", "123456"));

        assertThat(response.token()).isEqualTo("token-value");
        assertThat(response.userId()).isEqualTo(1L);
        assertThat(response.username()).isEqualTo("admin");
        assertThat(response.nickname()).isEqualTo("超级管理员");
        assertThat(response.permissions()).containsExactly("sys:user:list", "sys:role:list");
        verify(jwtService).generateToken(1L, "admin", permissions);
    }

    @Test
    void loginShouldThrowBusinessExceptionWhenUserNotFound() {
        when(userMapper.findByUsername("missing")).thenReturn(null);

        assertThatThrownBy(() -> authService.login(new LoginRequest("missing", "123456")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("用户名或密码错误");
    }

    @Test
    void loginShouldThrowBusinessExceptionWhenPasswordInvalid() {
        when(userMapper.findByUsername("admin")).thenReturn(adminUser(1));
        when(passwordEncoder.matches("bad-password", "encoded-password")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(new LoginRequest("admin", "bad-password")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("用户名或密码错误");
    }

    @Test
    void loginShouldThrowBusinessExceptionWhenUserDisabled() {
        when(userMapper.findByUsername("admin")).thenReturn(adminUser(0));
        when(passwordEncoder.matches("123456", "encoded-password")).thenReturn(true);

        assertThatThrownBy(() -> authService.login(new LoginRequest("admin", "123456")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("用户已被禁用");
    }

    private User adminUser(Integer status) {
        return new User(
                1L, "admin", "encoded-password", "超级管理员",
                "admin@example.com", "10000000000", 0, null,
                2L, status, "system", LocalDateTime.now(),
                "system", LocalDateTime.now(), 0
        );
    }
}
