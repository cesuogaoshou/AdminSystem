package com.example.admin.module.user;

import com.example.admin.common.BusinessException;
import com.example.admin.common.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final String DEFAULT_PASSWORD = "123456";

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User getById(Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return user;
    }

    public PageResult<UserVO> page(UserQueryRequest query) {
        PageHelper.startPage(query.pageOrDefault(), query.sizeOrDefault());
        try {
            List<UserVO> rows = userMapper.findPage(query);
            PageInfo<UserVO> pageInfo = new PageInfo<>(rows);
            return PageResult.of(pageInfo.getTotal(), rows);
        } finally {
            PageHelper.clearPage();
        }
    }

    public void create(UserSaveRequest request) {
        if (userMapper.countByUsername(request.username()) > 0) {
            throw new BusinessException(400, "用户名已存在");
        }

        User user = new User(
                null,
                request.username(),
                passwordEncoder.encode(DEFAULT_PASSWORD),
                request.nickname(),
                request.email(),
                request.phone(),
                request.gender(),
                request.avatar(),
                request.deptId(),
                request.status() == null ? 1 : request.status(),
                "system",
                null,
                "system",
                null,
                0
        );

        userMapper.insert(user);
    }

    public void update(Long id, UserSaveRequest request) {
        User existing = getById(id);

        User user = new User(
                existing.id(),
                existing.username(),
                existing.password(),
                request.nickname(),
                request.email(),
                request.phone(),
                request.gender(),
                request.avatar(),
                request.deptId(),
                request.status() == null ? existing.status() : request.status(),
                existing.createBy(),
                existing.createTime(),
                "system",
                existing.updateTime(),
                existing.deleted()
        );

        userMapper.update(user);
    }
}