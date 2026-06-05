package com.example.admin.module.user;

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

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void pageShouldReturnUnifiedResult() throws Exception {
        UserVO user = new UserVO(
                1L, "admin", "管理员", "admin@example.com",
                "10000000000", 0, null, 2L, "技术部",
                1, LocalDateTime.now()
        );
        when(userService.page(new UserQueryRequest(1, 10, "admin", 1, 2L)))
                .thenReturn(PageResult.of(1, List.of(user)));

        mockMvc.perform(get("/api/users")
                        .param("page", "1")
                        .param("size", "10")
                        .param("username", "admin")
                        .param("status", "1")
                        .param("deptId", "2"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.rows[0].username").value("admin"));
    }

    @Test
    void getByIdShouldReturnUnifiedResult() throws Exception {
        User user = new User(
                1L, "admin", "encoded-password", "管理员",
                "admin@example.com", "10000000000", 0, null,
                2L, 1, "system", LocalDateTime.now(),
                "system", LocalDateTime.now(), 0
        );
        when(userService.getById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("admin"));
    }

    @Test
    void createShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "new_user",
                                  "nickname": "新用户",
                                  "email": "new@example.com",
                                  "phone": "10000000001",
                                  "gender": 0,
                                  "deptId": 2,
                                  "status": 1
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(userService).create(org.mockito.ArgumentMatchers.any(UserSaveRequest.class));
    }

    @Test
    void updateShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "ignored",
                                  "nickname": "新昵称",
                                  "email": "new@example.com",
                                  "phone": "10000000001",
                                  "gender": 1,
                                  "deptId": 3,
                                  "status": 1
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(userService).update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any(UserSaveRequest.class));
    }

    @Test
    void deleteShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(jsonPath("$.code").value(200));

        verify(userService).delete(1L);
    }

    @Test
    void changeStatusShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(put("/api/users/1/status")
                        .param("status", "0"))
                .andExpect(jsonPath("$.code").value(200));

        verify(userService).changeStatus(1L, 0);
    }
}