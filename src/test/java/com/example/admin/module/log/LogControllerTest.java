package com.example.admin.module.log;

import com.example.admin.common.GlobalExceptionHandler;
import com.example.admin.common.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(LogController.class)
@Import(GlobalExceptionHandler.class)
class LogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogService logService;

    @Test
    void pageShouldReturnUnifiedResult() throws Exception {
        LogVO logVO = new LogVO(
                1L,
                "admin",
                "用户管理",
                "新增用户",
                "POST",
                "/api/users",
                "127.0.0.1",
                42L,
                1,
                LocalDateTime.now()
        );
        LogQueryRequest query = new LogQueryRequest(1, 10, "admin", "用户", 1, null, null);
        when(logService.page(query)).thenReturn(PageResult.of(1, List.of(logVO)));

        mockMvc.perform(get("/api/logs")
                        .param("page", "1")
                        .param("size", "10")
                        .param("username", "admin")
                        .param("module", "用户")
                        .param("status", "1"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.rows[0].username").value("admin"))
                .andExpect(jsonPath("$.data.rows[0].module").value("用户管理"));
    }
}
