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

@WebMvcTest(DictItemController.class)
@Import(GlobalExceptionHandler.class)
class DictItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DictItemService dictItemService;

    @Test
    void listByTypeIdShouldReturnUnifiedResult() throws Exception {
        when(dictItemService.listByTypeId(1L)).thenReturn(List.of(dictItem()));

        mockMvc.perform(get("/api/dict-types/1/items"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].label").value("男"));
    }

    @Test
    void createShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(post("/api/dict-types/1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "label": "处理中",
                                  "value": "processing",
                                  "color": "blue",
                                  "sortOrder": 1,
                                  "status": 1
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(dictItemService).create(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any(DictItemSaveRequest.class));
    }

    @Test
    void updateShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(put("/api/dict-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "label": "处理中",
                                  "value": "processing",
                                  "color": "blue",
                                  "sortOrder": 1,
                                  "status": 1
                                }
                                """))
                .andExpect(jsonPath("$.code").value(200));

        verify(dictItemService).update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any(DictItemSaveRequest.class));
    }

    @Test
    void deleteShouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(delete("/api/dict-items/1"))
                .andExpect(jsonPath("$.code").value(200));

        verify(dictItemService).delete(1L);
    }

    private DictItem dictItem() {
        LocalDateTime now = LocalDateTime.now();
        return new DictItem(1L, 1L, "男", "1", "blue", 1, 1, now, now);
    }
}
