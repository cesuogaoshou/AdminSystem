package com.example.admin.module.dict;

import com.example.admin.common.GlobalExceptionHandler;
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

@WebMvcTest(DictTypeController.class)
@Import(GlobalExceptionHandler.class)
class DictTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DictTypeService dictTypeService;

    @Test
    void listShouldReturnUnifiedResult() throws Exception {
        when(dictTypeService.list()).thenReturn(List.of(dictType()));

        mockMvc.perform(get("/api/dict-types"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].code").value("gender"));
    }

    @Test
    void getByIdShouldReturnUnifiedResult() throws Exception {
        when(dictTypeService.getById(1L)).thenReturn(dictType());

        mockMvc.perform(get("/api/dict-types/1"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.code").value("gender"));
    }

    @Test
    void createShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(post("/api/dict-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "业务状态",
                                  "code": "biz_status",
                                  "description": "业务状态字典",
                                  "status": 1
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(dictTypeService).create(org.mockito.ArgumentMatchers.any(DictTypeSaveRequest.class));
    }

    @Test
    void updateShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(put("/api/dict-types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "用户性别",
                                  "code": "gender",
                                  "description": "用户性别字典",
                                  "status": 1
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(dictTypeService).update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any(DictTypeSaveRequest.class));
    }

    @Test
    void deleteShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(delete("/api/dict-types/1"))
                .andExpect(jsonPath("$.code").value(200));

        verify(dictTypeService).delete(1L);
    }

    private DictType dictType() {
        return new DictType(
                1L,
                "用户性别",
                "gender",
                "用户性别字典",
                1,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
