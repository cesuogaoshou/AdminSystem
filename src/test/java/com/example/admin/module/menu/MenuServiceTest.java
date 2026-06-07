package com.example.admin.module.menu;

import com.example.admin.common.BusinessException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuMapper menuMapper;

    @InjectMocks
    private MenuService menuService;

    @Test
    void treeShouldBuildMenuHierarchy() {
        LocalDateTime now = LocalDateTime.now();
        List<Menu> menus = List.of(
                new Menu(1L, 0L, "系统管理", 1, "/system", null, null, "setting", 1, 1, now, now),
                new Menu(2L, 1L, "用户管理", 2, "/system/users", "system/user/index", "sys:user:list", "user", 1, 1, now, now),
                new Menu(3L, 2L, "用户新增", 3, null, null, "sys:user:add", null, 1, 1, now, now)
        );
        when(menuMapper.findAll()).thenReturn(menus);

        List<MenuVO> tree = menuService.tree();

        assertThat(tree).hasSize(1);
        assertThat(tree.getFirst().name()).isEqualTo("系统管理");
        assertThat(tree.getFirst().children()).hasSize(1);
        assertThat(tree.getFirst().children().getFirst().children())
                .extracting(MenuVO::permission)
                .containsExactly("sys:user:add");
    }

    @Test
    void treeShouldReturnEmptyListWhenNoMenuExists() {
        when(menuMapper.findAll()).thenReturn(List.of());

        List<MenuVO> tree = menuService.tree();

        assertThat(tree).isEmpty();
    }

    @Test
    void createShouldInsertMenuWithDefaults() {
        MenuSaveRequest request = new MenuSaveRequest(
                null, "审计日志", 2, "/system/logs", "system/log/index",
                "sys:log:list", "log", null, null
        );

        menuService.create(request);

        verify(menuMapper).insert(argThat(menu ->
                menu.parentId().equals(0L)
                        && menu.name().equals("审计日志")
                        && menu.type().equals(2)
                        && menu.path().equals("/system/logs")
                        && menu.component().equals("system/log/index")
                        && menu.permission().equals("sys:log:list")
                        && menu.icon().equals("log")
                        && menu.sortOrder().equals(0)
                        && menu.visible().equals(1)
        ));
    }

    @Test
    void updateShouldUpdateMenuWhenMenuExists() {
        LocalDateTime now = LocalDateTime.now();
        Menu existing = new Menu(
                2L, 1L, "用户管理", 2, "/system/users", "system/user/index",
                "sys:user:list", "user", 1, 1, now, now
        );
        MenuSaveRequest request = new MenuSaveRequest(
                1L, "账号管理", 2, "/system/accounts", "system/account/index",
                "sys:account:list", "account", 2, 1
        );
        when(menuMapper.findById(2L)).thenReturn(existing);

        menuService.update(2L, request);

        verify(menuMapper).update(argThat(menu ->
                menu.id().equals(2L)
                        && menu.parentId().equals(1L)
                        && menu.name().equals("账号管理")
                        && menu.path().equals("/system/accounts")
                        && menu.permission().equals("sys:account:list")
                        && menu.sortOrder().equals(2)
                        && menu.visible().equals(1)
        ));
    }

    @Test
    void updateShouldThrowBusinessExceptionWhenMenuNotFound() {
        MenuSaveRequest request = new MenuSaveRequest(
                1L, "账号管理", 2, "/system/accounts", "system/account/index",
                "sys:account:list", "account", 2, 1
        );
        when(menuMapper.findById(99L)).thenReturn(null);

        assertThatThrownBy(() -> menuService.update(99L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("菜单不存在");
    }

    @Test
    void updateShouldThrowBusinessExceptionWhenParentIsSelf() {
        MenuSaveRequest request = new MenuSaveRequest(
                2L, "账号管理", 2, "/system/accounts", "system/account/index",
                "sys:account:list", "account", 2, 1
        );

        assertThatThrownBy(() -> menuService.update(2L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("上级菜单不能是自己");
    }

    @Test
    void deleteShouldDeleteMenuWhenNoChildAndNoRole() {
        LocalDateTime now = LocalDateTime.now();
        Menu existing = new Menu(
                3L, 2L, "用户新增", 3, null, null,
                "sys:user:add", null, 1, 1, now, now
        );
        when(menuMapper.findById(3L)).thenReturn(existing);
        when(menuMapper.countByParentId(3L)).thenReturn(0);
        when(menuMapper.countRolesByMenuId(3L)).thenReturn(0);

        menuService.delete(3L);

        verify(menuMapper).deleteById(3L);
    }

    @Test
    void deleteShouldThrowBusinessExceptionWhenMenuHasChildren() {
        when(menuMapper.findById(1L)).thenReturn(new Menu(
                1L, 0L, "系统管理", 1, "/system", null,
                null, "setting", 1, 1, LocalDateTime.now(), LocalDateTime.now()
        ));
        when(menuMapper.countByParentId(1L)).thenReturn(4);

        assertThatThrownBy(() -> menuService.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("存在子菜单，不能删除");
    }

    @Test
    void deleteShouldThrowBusinessExceptionWhenMenuAssignedToRoles() {
        when(menuMapper.findById(3L)).thenReturn(new Menu(
                3L, 2L, "用户新增", 3, null, null,
                "sys:user:add", null, 1, 1, LocalDateTime.now(), LocalDateTime.now()
        ));
        when(menuMapper.countByParentId(3L)).thenReturn(0);
        when(menuMapper.countRolesByMenuId(3L)).thenReturn(1);

        assertThatThrownBy(() -> menuService.delete(3L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("菜单已分配给角色，不能删除");
    }
}
