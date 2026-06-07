package com.example.admin.module.role;

import com.example.admin.common.GlobalExceptionHandler;
import com.example.admin.common.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RoleController.class)
@Import(GlobalExceptionHandler.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    @Test
    void pageShouldReturnUnifiedResult() throws Exception {
        RoleVO role = new RoleVO(
                1L, "超级管理员", "admin", "拥有全部权限",
                1, 1, LocalDateTime.now()
        );
        when(roleService.page(new RoleQueryRequest(1, 10, "admin", 1)))
                .thenReturn(PageResult.of(1, List.of(role)));

        mockMvc.perform(get("/api/roles")
                        .param("page", "1")
                        .param("size", "10")
                        .param("name", "admin")
                        .param("status", "1"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.rows[0].code").value("admin"));
    }

    @Test
    void getByIdShouldReturnUnifiedResult() throws Exception {
        Role role = new Role(
                1L, "超级管理员", "admin", "拥有全部权限",
                1, 1, LocalDateTime.now(), LocalDateTime.now()
        );
        when(roleService.getById(1L)).thenReturn(role);

        mockMvc.perform(get("/api/roles/1"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.code").value("admin"));
    }

    @Test
    void createShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "审计员",
                                  "code": "auditor",
                                  "description": "查看审计日志",
                                  "status": 1,
                                  "sortOrder": 3
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(roleService).create(org.mockito.ArgumentMatchers.any(RoleSaveRequest.class));
    }

    @Test
    void updateShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(put("/api/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "系统管理员",
                                  "code": "system_admin",
                                  "description": "管理系统基础数据",
                                  "status": 1,
                                  "sortOrder": 2
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(roleService).update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any(RoleSaveRequest.class));
    }

    @Test
    void deleteShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(delete("/api/roles/2"))
                .andExpect(jsonPath("$.code").value(200));

        verify(roleService).delete(2L);
    }

    @Test
    void changeStatusShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(put("/api/roles/1/status")
                        .param("status", "0"))
                .andExpect(jsonPath("$.code").value(200));

        verify(roleService).changeStatus(1L, 0);
    }

    @Test
    void getMenuIdsShouldReturnUnifiedResult() throws Exception {
        when(roleService.getMenuIds(1L)).thenReturn(List.of(1L, 2L, 3L));

        mockMvc.perform(get("/api/roles/1/menus"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0]").value(1))
                .andExpect(jsonPath("$.data[1]").value(2))
                .andExpect(jsonPath("$.data[2]").value(3));
    }

    @Test
    void assignMenusShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(put("/api/roles/1/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "menuIds": [1, 2, 3]
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(roleService).assignMenus(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any(RoleMenuAssignRequest.class));
    }
}
