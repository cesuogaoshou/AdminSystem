package com.example.admin.module.dict;

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
class DictItemServiceTest {

    @Mock
    private DictItemMapper dictItemMapper;

    @Mock
    private DictTypeMapper dictTypeMapper;

    @Mock
    private DictCacheService dictCacheService;

    @InjectMocks
    private DictItemService dictItemService;

    @Test
    void listByTypeIdShouldReturnDictItemsWhenTypeExists() {
        when(dictTypeMapper.findById(1L)).thenReturn(dictType());
        when(dictItemMapper.findByTypeId(1L)).thenReturn(List.of(dictItem()));

        List<DictItem> result = dictItemService.listByTypeId(1L);

        assertThat(result).containsExactly(dictItem());
        verify(dictItemMapper).findByTypeId(1L);
    }

    @Test
    void createShouldInsertDictItemWhenTypeExistsAndValueNotDuplicated() {
        DictItemSaveRequest request = new DictItemSaveRequest("处理中", "processing", "blue", null, null);
        when(dictTypeMapper.findById(2L)).thenReturn(new DictType(2L, "通用状态", "status", "状态", 1, null, null));
        when(dictItemMapper.findByTypeIdAndValue(2L, "processing")).thenReturn(null);

        dictItemService.create(2L, request);

        verify(dictItemMapper).insert(argThat(item ->
                item.typeId().equals(2L)
                        && item.label().equals("处理中")
                        && item.value().equals("processing")
                        && item.color().equals("blue")
                        && item.sortOrder().equals(0)
                        && item.status().equals(1)
        ));
        verify(dictCacheService).evictItems("status");
    }

    @Test
    void createShouldThrowBusinessExceptionWhenValueDuplicated() {
        DictItemSaveRequest request = new DictItemSaveRequest("男", "1", "blue", 1, 1);
        when(dictTypeMapper.findById(1L)).thenReturn(dictType());
        when(dictItemMapper.findByTypeIdAndValue(1L, "1")).thenReturn(dictItem());

        assertThatThrownBy(() -> dictItemService.create(1L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("字典项值已存在");
    }

    @Test
    void updateShouldUpdateDictItemWhenValueNotDuplicated() {
        DictItem existing = dictItem();
        DictItemSaveRequest request = new DictItemSaveRequest("男-更新", "1", "cyan", 2, 0);
        when(dictItemMapper.findById(1L)).thenReturn(existing);
        when(dictItemMapper.findByTypeIdAndValue(1L, "1")).thenReturn(existing);
        when(dictTypeMapper.findById(1L)).thenReturn(dictType());

        dictItemService.update(1L, request);

        verify(dictItemMapper).update(argThat(item ->
                item.id().equals(1L)
                        && item.typeId().equals(1L)
                        && item.label().equals("男-更新")
                        && item.value().equals("1")
                        && item.color().equals("cyan")
                        && item.sortOrder().equals(2)
                        && item.status().equals(0)
        ));
        verify(dictCacheService).evictItems("gender");
    }

    @Test
    void updateShouldThrowBusinessExceptionWhenItemMissing() {
        DictItemSaveRequest request = new DictItemSaveRequest("男", "1", "blue", 1, 1);
        when(dictItemMapper.findById(99L)).thenReturn(null);

        assertThatThrownBy(() -> dictItemService.update(99L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("字典项不存在");
    }

    @Test
    void deleteShouldDeleteDictItemWhenExists() {
        when(dictItemMapper.findById(1L)).thenReturn(dictItem());
        when(dictTypeMapper.findById(1L)).thenReturn(dictType());

        dictItemService.delete(1L);

        verify(dictItemMapper).deleteById(1L);
        verify(dictCacheService).evictItems("gender");
    }

    @Test
    void listByTypeCodeShouldReturnCachedItemsWhenCacheExists() {
        when(dictCacheService.getItems("gender")).thenReturn(List.of(dictItem()));

        List<DictItem> result = dictItemService.listByTypeCode("gender");

        assertThat(result).containsExactly(dictItem());
    }

    @Test
    void listByTypeCodeShouldQueryDatabaseAndCacheWhenCacheMissing() {
        when(dictCacheService.getItems("gender")).thenReturn(null);
        when(dictTypeMapper.findByCode("gender")).thenReturn(dictType());
        when(dictItemMapper.findByTypeCode("gender")).thenReturn(List.of(dictItem()));

        List<DictItem> result = dictItemService.listByTypeCode("gender");

        assertThat(result).containsExactly(dictItem());
        verify(dictItemMapper).findByTypeCode("gender");
        verify(dictCacheService).putItems("gender", List.of(dictItem()));
    }

    @Test
    void listByTypeCodeShouldThrowBusinessExceptionWhenTypeCodeMissing() {
        when(dictCacheService.getItems("missing")).thenReturn(null);
        when(dictTypeMapper.findByCode("missing")).thenReturn(null);

        assertThatThrownBy(() -> dictItemService.listByTypeCode("missing"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("字典类型不存在");
    }

    private DictType dictType() {
        return new DictType(1L, "用户性别", "gender", "用户性别字典", 1, null, null);
    }

    private DictItem dictItem() {
        LocalDateTime now = LocalDateTime.of(2026, 6, 8, 19, 0);
        return new DictItem(1L, 1L, "男", "1", "blue", 1, 1, now, now);
    }
}
