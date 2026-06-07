package com.example.admin.module.menu;

import com.example.admin.common.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MenuController.class)
@Import(GlobalExceptionHandler.class)
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuService menuService;

    @Test
    void treeShouldReturnUnifiedResult() throws Exception {
        MenuVO child = new MenuVO(
                2L, 1L, "用户管理", 2, "/system/users",
                "system/user/index", "sys:user:list", "user",
                1, 1, List.of()
        );
        MenuVO root = new MenuVO(
                1L, 0L, "系统管理", 1, "/system",
                null, null, "setting", 1, 1, List.of(child)
        );
        when(menuService.tree()).thenReturn(List.of(root));

        mockMvc.perform(get("/api/menus/tree"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].name").value("系统管理"))
                .andExpect(jsonPath("$.data[0].children[0].permission").value("sys:user:list"));
    }

    @Test
    void createShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "parentId": 1,
                                  "name": "审计日志",
                                  "type": 2,
                                  "path": "/system/logs",
                                  "component": "system/log/index",
                                  "permission": "sys:log:list",
                                  "icon": "log",
                                  "sortOrder": 5,
                                  "visible": 1
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(menuService).create(org.mockito.ArgumentMatchers.any(MenuSaveRequest.class));
    }

    @Test
    void updateShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(put("/api/menus/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "parentId": 1,
                                  "name": "账号管理",
                                  "type": 2,
                                  "path": "/system/accounts",
                                  "component": "system/account/index",
                                  "permission": "sys:account:list",
                                  "icon": "account",
                                  "sortOrder": 2,
                                  "visible": 1
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(menuService).update(org.mockito.ArgumentMatchers.eq(2L), org.mockito.ArgumentMatchers.any(MenuSaveRequest.class));
    }

    @Test
    void deleteShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(delete("/api/menus/3"))
                .andExpect(jsonPath("$.code").value(200));

        verify(menuService).delete(3L);
    }
}
