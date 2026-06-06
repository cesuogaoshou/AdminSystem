package com.example.admin.module.dept;

import com.example.admin.common.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import org.springframework.http.MediaType;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(DeptController.class)
@Import(GlobalExceptionHandler.class)
class DeptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeptService deptService;

    @Test
    void treeShouldReturnUnifiedResult() throws Exception {
        DeptVO child = new DeptVO(
                2L, 1L, "技术部", "CTO", "10000000001",
                2, 1, List.of()
        );
        DeptVO root = new DeptVO(
                1L, 0L, "总公司", "CEO", "10000000000",
                1, 1, List.of(child)
        );
        when(deptService.tree()).thenReturn(List.of(root));

        mockMvc.perform(get("/api/depts/tree"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].name").value("总公司"))
                .andExpect(jsonPath("$.data[0].children[0].name").value("技术部"));
    }

    @Test
    void createShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(post("/api/depts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "parentId": 1,
                                  "name": "研发部",
                                  "leader": "研发负责人",
                                  "phone": "10000000003",
                                  "sortOrder": 4,
                                  "status": 1
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(deptService).create(org.mockito.ArgumentMatchers.any(DeptSaveRequest.class));
    }

    @Test
    void updateShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(put("/api/depts/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "parentId": 1,
                                  "name": "研发部",
                                  "leader": "研发负责人",
                                  "phone": "10000000003",
                                  "sortOrder": 4,
                                  "status": 1
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(deptService).update(org.mockito.ArgumentMatchers.eq(2L), org.mockito.ArgumentMatchers.any(DeptSaveRequest.class));
    }

    @Test
    void deleteShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(delete("/api/depts/3"))
                .andExpect(jsonPath("$.code").value(200));

        verify(deptService).delete(3L);
    }
}