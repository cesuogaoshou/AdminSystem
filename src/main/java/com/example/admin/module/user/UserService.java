package com.example.admin.module.user;

import com.example.admin.common.BusinessException;
import com.example.admin.common.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
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
}