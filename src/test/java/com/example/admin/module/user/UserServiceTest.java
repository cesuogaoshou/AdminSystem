package com.example.admin.module.user;

import com.example.admin.common.BusinessException;
import com.example.admin.common.PageResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getByIdShouldReturnUserWhenUserExists() {
        User user = new User(
                1L, "admin", "encoded-password", "管理员",
                "admin@example.com", "10000000000", 0, null,
                2L, 1, "system", LocalDateTime.now(),
                "system", LocalDateTime.now(), 0
        );
        when(userMapper.findById(1L)).thenReturn(user);

        User result = userService.getById(1L);

        assertThat(result).isEqualTo(user);
        verify(userMapper).findById(1L);
    }

    @Test
    void getByIdShouldThrowBusinessExceptionWhenUserNotFound() {
        when(userMapper.findById(99L)).thenReturn(null);

        assertThatThrownBy(() -> userService.getById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("用户不存在");
    }

    @Test
    void pageShouldReturnPageResult() {
        UserQueryRequest query = new UserQueryRequest(1, 10, "admin", 1, 2L);
        UserVO userVO = new UserVO(
                1L, "admin", "管理员", "admin@example.com",
                "10000000000", 0, null, 2L, "技术部",
                1, LocalDateTime.now()
        );
        when(userMapper.findPage(query)).thenReturn(List.of(userVO));

        PageResult<UserVO> result = userService.page(query);

        assertThat(result.total()).isEqualTo(1);
        assertThat(result.rows()).containsExactly(userVO);
        verify(userMapper).findPage(query);
    }
}