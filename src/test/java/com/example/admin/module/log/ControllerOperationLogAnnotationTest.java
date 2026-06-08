package com.example.admin.module.log;

import com.example.admin.module.dept.DeptController;
import com.example.admin.module.menu.MenuController;
import com.example.admin.module.role.RoleController;
import com.example.admin.module.user.UserController;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerOperationLogAnnotationTest {

    @Test
    void userControllerWriteMethodsShouldHaveOperationLog() throws NoSuchMethodException {
        assertOperationLog(UserController.class, "create", "用户管理", "新增用户");
        assertOperationLog(UserController.class, "update", "用户管理", "修改用户");
        assertOperationLog(UserController.class, "delete", "用户管理", "删除用户");
        assertOperationLog(UserController.class, "changeStatus", "用户管理", "修改用户状态");
        assertOperationLog(UserController.class, "assignRoles", "用户管理", "分配用户角色");
    }

    @Test
    void roleControllerWriteMethodsShouldHaveOperationLog() throws NoSuchMethodException {
        assertOperationLog(RoleController.class, "create", "角色管理", "新增角色");
        assertOperationLog(RoleController.class, "update", "角色管理", "修改角色");
        assertOperationLog(RoleController.class, "delete", "角色管理", "删除角色");
        assertOperationLog(RoleController.class, "changeStatus", "角色管理", "修改角色状态");
        assertOperationLog(RoleController.class, "assignMenus", "角色管理", "分配角色菜单");
    }

    @Test
    void menuControllerWriteMethodsShouldHaveOperationLog() throws NoSuchMethodException {
        assertOperationLog(MenuController.class, "create", "菜单管理", "新增菜单");
        assertOperationLog(MenuController.class, "update", "菜单管理", "修改菜单");
        assertOperationLog(MenuController.class, "delete", "菜单管理", "删除菜单");
    }

    @Test
    void deptControllerWriteMethodsShouldHaveOperationLog() throws NoSuchMethodException {
        assertOperationLog(DeptController.class, "create", "部门管理", "新增部门");
        assertOperationLog(DeptController.class, "update", "部门管理", "修改部门");
        assertOperationLog(DeptController.class, "delete", "部门管理", "删除部门");
    }

    private void assertOperationLog(
            Class<?> controllerClass,
            String methodName,
            String expectedModule,
            String expectedOperation
    ) throws NoSuchMethodException {
        Method method = findMethod(controllerClass, methodName);
        OperationLog operationLog = method.getAnnotation(OperationLog.class);

        assertThat(operationLog).isNotNull();
        assertThat(operationLog.module()).isEqualTo(expectedModule);
        assertThat(operationLog.operation()).isEqualTo(expectedOperation);
    }

    private Method findMethod(Class<?> controllerClass, String methodName) throws NoSuchMethodException {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new NoSuchMethodException(controllerClass.getName() + "." + methodName);
    }
}
