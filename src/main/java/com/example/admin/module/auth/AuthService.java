package com.example.admin.module.auth;

import com.example.admin.common.BusinessException;
import com.example.admin.module.user.User;
import com.example.admin.module.user.UserMapper;
import com.example.admin.security.CurrentUser;
import com.example.admin.security.CurrentUserContext;
import com.example.admin.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByUsername(request.username());
        if (user == null || !passwordEncoder.matches(request.password(), user.password())) {
            throw new BusinessException(400, "用户名或密码错误");
        }

        if (user.status() == null || user.status() != 1) {
            throw new BusinessException(400, "用户已被禁用");
        }

        List<String> permissions = userMapper.findPermissionsByUserId(user.id());
        String token = jwtService.generateToken(user.id(), user.username(), permissions);

        return new LoginResponse(
                token,
                user.id(),
                user.username(),
                user.nickname(),
                permissions
        );
    }

    public CurrentUserResponse currentUser() {
        CurrentUser currentUser = CurrentUserContext.get();
        if (currentUser == null) {
            throw new BusinessException(401, "未登录");
        }

        return new CurrentUserResponse(
                currentUser.userId(),
                currentUser.username(),
                currentUser.permissions()
        );
    }
}
