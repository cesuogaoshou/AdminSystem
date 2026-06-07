package com.example.admin.module.user;

import com.example.admin.common.BusinessException;
import com.example.admin.common.PageResult;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

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

    @Test
    void createShouldInsertUserWhenUsernameNotExists() {
        UserSaveRequest request = new UserSaveRequest(
                "new_user", "新用户", "new@example.com", "10000000001",
                0, null, 2L, 1
        );
        when(userMapper.countByUsername("new_user")).thenReturn(0);
        when(passwordEncoder.encode("123456")).thenReturn("encoded-default-password");

        userService.create(request);

        verify(userMapper).countByUsername("new_user");
        verify(passwordEncoder).encode("123456");
        verify(userMapper).insert(argThat(user ->
                user.username().equals("new_user")
                        && user.password().equals("encoded-default-password")
                        && user.nickname().equals("新用户")
                        && user.status().equals(1)
                        && user.deleted().equals(0)
        ));
    }

    @Test
    void createShouldThrowBusinessExceptionWhenUsernameExists() {
        UserSaveRequest request = new UserSaveRequest(
                "admin", "管理员", "admin@example.com", "10000000000",
                0, null, 2L, 1
        );
        when(userMapper.countByUsername("admin")).thenReturn(1);

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("用户名已存在");
    }

    @Test
    void updateShouldUpdateUserWhenUserExists() {
        User existing = new User(
                1L, "admin", "encoded-password", "管理员",
                "admin@example.com", "10000000000", 0, null,
                2L, 1, "system", LocalDateTime.now(),
                "system", LocalDateTime.now(), 0
        );
        UserSaveRequest request = new UserSaveRequest(
                "ignored_username", "新昵称", "new@example.com", "10000000001",
                1, "https://example.com/avatar.png", 3L, 1
        );
        when(userMapper.findById(1L)).thenReturn(existing);

        userService.update(1L, request);

        verify(userMapper).findById(1L);
        verify(userMapper).update(argThat(user ->
                user.id().equals(1L)
                        && user.username().equals("admin")
                        && user.password().equals("encoded-password")
                        && user.nickname().equals("新昵称")
                        && user.email().equals("new@example.com")
                        && user.phone().equals("10000000001")
                        && user.gender().equals(1)
                        && user.avatar().equals("https://example.com/avatar.png")
                        && user.deptId().equals(3L)
                        && user.status().equals(1)
                        && user.deleted().equals(0)
        ));
    }

    @Test
    void updateShouldThrowBusinessExceptionWhenUserNotFound() {
        UserSaveRequest request = new UserSaveRequest(
                "ignored_username", "新昵称", "new@example.com", "10000000001",
                1, null, 3L, 1
        );
        when(userMapper.findById(99L)).thenReturn(null);

        assertThatThrownBy(() -> userService.update(99L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("用户不存在");
    }

    @Test
    void deleteShouldMarkUserDeletedWhenUserExists() {
        User existing = new User(
                2L, "test_user", "encoded-password", "测试用户",
                "test@example.com", "10000000002", 0, null,
                2L, 1, "system", LocalDateTime.now(),
                "system", LocalDateTime.now(), 0
        );
        when(userMapper.findById(2L)).thenReturn(existing);

        userService.delete(2L);

        verify(userMapper).findById(2L);
        verify(userMapper).deleteById(2L);
    }

    @Test
    void deleteShouldThrowBusinessExceptionWhenUserNotFound() {
        when(userMapper.findById(99L)).thenReturn(null);

        assertThatThrownBy(() -> userService.delete(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("用户不存在");
    }

    @Test
    void changeStatusShouldUpdateUserStatusWhenUserExists() {
        User existing = new User(
                2L, "test_user", "encoded-password", "测试用户",
                "test@example.com", "10000000002", 0, null,
                2L, 1, "system", LocalDateTime.now(),
                "system", LocalDateTime.now(), 0
        );
        when(userMapper.findById(2L)).thenReturn(existing);

        userService.changeStatus(2L, 0);

        verify(userMapper).findById(2L);
        verify(userMapper).updateStatus(2L, 0);
    }

    @Test
    void changeStatusShouldThrowBusinessExceptionWhenStatusInvalid() {
        assertThatThrownBy(() -> userService.changeStatus(2L, 9))
                .isInstanceOf(BusinessException.class)
                .hasMessage("用户状态不正确");
    }

    @Test
    void getRoleIdsShouldReturnUserRoleIdsWhenUserExists() {
        when(userMapper.findById(1L)).thenReturn(adminUser());
        when(userMapper.findRoleIdsByUserId(1L)).thenReturn(List.of(1L, 2L));

        List<Long> roleIds = userService.getRoleIds(1L);

        assertThat(roleIds).containsExactly(1L, 2L);
        verify(userMapper).findRoleIdsByUserId(1L);
    }

    @Test
    void assignRolesShouldReplaceUserRolesWhenUserExists() {
        when(userMapper.findById(1L)).thenReturn(adminUser());
        UserRoleAssignRequest request = new UserRoleAssignRequest(List.of(1L, 2L));

        userService.assignRoles(1L, request);

        verify(userMapper).deleteRolesByUserId(1L);
        verify(userMapper).insertUserRoles(1L, List.of(1L, 2L));
    }

    @Test
    void assignRolesShouldOnlyDeleteWhenRoleIdsIsEmpty() {
        when(userMapper.findById(1L)).thenReturn(adminUser());
        UserRoleAssignRequest request = new UserRoleAssignRequest(List.of());

        userService.assignRoles(1L, request);

        verify(userMapper).deleteRolesByUserId(1L);
        verify(userMapper, never()).insertUserRoles(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    void assignRolesShouldThrowBusinessExceptionWhenUserNotFound() {
        when(userMapper.findById(99L)).thenReturn(null);
        UserRoleAssignRequest request = new UserRoleAssignRequest(List.of(1L));

        assertThatThrownBy(() -> userService.assignRoles(99L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("用户不存在");
    }

    private User adminUser() {
        return new User(
                1L, "admin", "encoded-password", "管理员",
                "admin@example.com", "10000000000", 0, null,
                2L, 1, "system", LocalDateTime.now(),
                "system", LocalDateTime.now(), 0
        );
    }
}
