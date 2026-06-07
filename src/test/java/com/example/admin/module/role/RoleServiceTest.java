package com.example.admin.module.role;

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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    @Test
    void getByIdShouldReturnRoleWhenRoleExists() {
        Role role = adminRole();
        when(roleMapper.findById(1L)).thenReturn(role);

        Role result = roleService.getById(1L);

        assertThat(result).isEqualTo(role);
        verify(roleMapper).findById(1L);
    }

    @Test
    void getByIdShouldThrowBusinessExceptionWhenRoleNotFound() {
        when(roleMapper.findById(99L)).thenReturn(null);

        assertThatThrownBy(() -> roleService.getById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("角色不存在");
    }

    @Test
    void pageShouldReturnPageResult() {
        RoleQueryRequest query = new RoleQueryRequest(1, 10, "admin", 1);
        RoleVO roleVO = new RoleVO(
                1L, "超级管理员", "admin", "拥有全部权限",
                1, 1, LocalDateTime.now()
        );
        when(roleMapper.findPage(query)).thenReturn(List.of(roleVO));

        PageResult<RoleVO> result = roleService.page(query);

        assertThat(result.total()).isEqualTo(1);
        assertThat(result.rows()).containsExactly(roleVO);
        verify(roleMapper).findPage(query);
    }

    @Test
    void createShouldInsertRoleWithDefaults() {
        RoleSaveRequest request = new RoleSaveRequest(
                "审计员", "auditor", "查看审计日志", null, null
        );
        when(roleMapper.countByCode("auditor")).thenReturn(0);

        roleService.create(request);

        verify(roleMapper).countByCode("auditor");
        verify(roleMapper).insert(argThat(role ->
                role.name().equals("审计员")
                        && role.code().equals("auditor")
                        && role.description().equals("查看审计日志")
                        && role.status().equals(1)
                        && role.sortOrder().equals(0)
        ));
    }

    @Test
    void createShouldThrowBusinessExceptionWhenCodeExists() {
        RoleSaveRequest request = new RoleSaveRequest(
                "超级管理员", "admin", "拥有全部权限", 1, 1
        );
        when(roleMapper.countByCode("admin")).thenReturn(1);

        assertThatThrownBy(() -> roleService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("角色编码已存在");
    }

    @Test
    void updateShouldUpdateRoleWhenRoleExistsAndCodeNotDuplicated() {
        Role existing = adminRole();
        RoleSaveRequest request = new RoleSaveRequest(
                "系统管理员", "system_admin", "管理系统基础数据", 1, 2
        );
        when(roleMapper.findById(1L)).thenReturn(existing);
        when(roleMapper.findByCode("system_admin")).thenReturn(null);

        roleService.update(1L, request);

        verify(roleMapper).findById(1L);
        verify(roleMapper).findByCode("system_admin");
        verify(roleMapper).update(argThat(role ->
                role.id().equals(1L)
                        && role.name().equals("系统管理员")
                        && role.code().equals("system_admin")
                        && role.description().equals("管理系统基础数据")
                        && role.status().equals(1)
                        && role.sortOrder().equals(2)
        ));
    }

    @Test
    void updateShouldAllowKeepingOriginalCode() {
        Role existing = adminRole();
        RoleSaveRequest request = new RoleSaveRequest(
                "系统管理员", "admin", "管理系统基础数据", null, null
        );
        when(roleMapper.findById(1L)).thenReturn(existing);
        when(roleMapper.findByCode("admin")).thenReturn(existing);

        roleService.update(1L, request);

        verify(roleMapper).update(argThat(role ->
                role.id().equals(1L)
                        && role.code().equals("admin")
                        && role.status().equals(1)
                        && role.sortOrder().equals(1)
        ));
    }

    @Test
    void updateShouldThrowBusinessExceptionWhenCodeBelongsToAnotherRole() {
        Role existing = adminRole();
        Role duplicate = new Role(
                2L, "普通用户", "user", "拥有基础查看权限",
                1, 2, LocalDateTime.now(), LocalDateTime.now()
        );
        RoleSaveRequest request = new RoleSaveRequest(
                "系统管理员", "user", "管理系统基础数据", 1, 2
        );
        when(roleMapper.findById(1L)).thenReturn(existing);
        when(roleMapper.findByCode("user")).thenReturn(duplicate);

        assertThatThrownBy(() -> roleService.update(1L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("角色编码已存在");
    }

    @Test
    void deleteShouldDeleteRoleWhenRoleHasNoUsers() {
        when(roleMapper.findById(2L)).thenReturn(new Role(
                2L, "普通用户", "user", "拥有基础查看权限",
                1, 2, LocalDateTime.now(), LocalDateTime.now()
        ));
        when(roleMapper.countUsersByRoleId(2L)).thenReturn(0);

        roleService.delete(2L);

        verify(roleMapper).deleteById(2L);
    }

    @Test
    void deleteShouldThrowBusinessExceptionWhenRoleHasUsers() {
        when(roleMapper.findById(1L)).thenReturn(adminRole());
        when(roleMapper.countUsersByRoleId(1L)).thenReturn(1);

        assertThatThrownBy(() -> roleService.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("角色已分配给用户，不能删除");
    }

    @Test
    void changeStatusShouldUpdateRoleStatusWhenRoleExists() {
        when(roleMapper.findById(1L)).thenReturn(adminRole());

        roleService.changeStatus(1L, 0);

        verify(roleMapper).updateStatus(1L, 0);
    }

    @Test
    void changeStatusShouldThrowBusinessExceptionWhenStatusInvalid() {
        assertThatThrownBy(() -> roleService.changeStatus(1L, 9))
                .isInstanceOf(BusinessException.class)
                .hasMessage("角色状态不正确");
    }

    @Test
    void getMenuIdsShouldReturnRoleMenuIdsWhenRoleExists() {
        when(roleMapper.findById(1L)).thenReturn(adminRole());
        when(roleMapper.findMenuIdsByRoleId(1L)).thenReturn(List.of(1L, 2L, 3L));

        List<Long> menuIds = roleService.getMenuIds(1L);

        assertThat(menuIds).containsExactly(1L, 2L, 3L);
        verify(roleMapper).findMenuIdsByRoleId(1L);
    }

    @Test
    void assignMenusShouldReplaceRoleMenusWhenRoleExists() {
        when(roleMapper.findById(1L)).thenReturn(adminRole());
        RoleMenuAssignRequest request = new RoleMenuAssignRequest(List.of(1L, 2L, 3L));

        roleService.assignMenus(1L, request);

        verify(roleMapper).deleteMenusByRoleId(1L);
        verify(roleMapper).insertRoleMenus(1L, List.of(1L, 2L, 3L));
    }

    @Test
    void assignMenusShouldOnlyDeleteWhenMenuIdsIsEmpty() {
        when(roleMapper.findById(1L)).thenReturn(adminRole());
        RoleMenuAssignRequest request = new RoleMenuAssignRequest(List.of());

        roleService.assignMenus(1L, request);

        verify(roleMapper).deleteMenusByRoleId(1L);
        verify(roleMapper, never()).insertRoleMenus(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    void assignMenusShouldThrowBusinessExceptionWhenRoleNotFound() {
        when(roleMapper.findById(99L)).thenReturn(null);
        RoleMenuAssignRequest request = new RoleMenuAssignRequest(List.of(1L));

        assertThatThrownBy(() -> roleService.assignMenus(99L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("角色不存在");
    }

    private Role adminRole() {
        return new Role(
                1L, "超级管理员", "admin", "拥有全部权限",
                1, 1, LocalDateTime.now(), LocalDateTime.now()
        );
    }
}
