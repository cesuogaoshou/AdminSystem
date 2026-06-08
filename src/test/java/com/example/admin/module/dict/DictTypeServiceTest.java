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
class DictTypeServiceTest {

    @Mock
    private DictTypeMapper dictTypeMapper;

    @Mock
    private DictCacheService dictCacheService;

    @InjectMocks
    private DictTypeService dictTypeService;

    @Test
    void listShouldReturnAllDictTypes() {
        DictType dictType = dictType();
        when(dictTypeMapper.findAll()).thenReturn(List.of(dictType));

        List<DictType> result = dictTypeService.list();

        assertThat(result).containsExactly(dictType);
        verify(dictTypeMapper).findAll();
    }

    @Test
    void getByIdShouldReturnDictTypeWhenExists() {
        DictType dictType = dictType();
        when(dictTypeMapper.findById(1L)).thenReturn(dictType);

        DictType result = dictTypeService.getById(1L);

        assertThat(result).isEqualTo(dictType);
    }

    @Test
    void getByIdShouldThrowBusinessExceptionWhenMissing() {
        when(dictTypeMapper.findById(99L)).thenReturn(null);

        assertThatThrownBy(() -> dictTypeService.getById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("字典类型不存在");
    }

    @Test
    void createShouldInsertDictTypeWhenCodeNotExists() {
        DictTypeSaveRequest request = new DictTypeSaveRequest("业务状态", "biz_status", "业务状态字典", null);
        when(dictTypeMapper.findByCode("biz_status")).thenReturn(null);

        dictTypeService.create(request);

        verify(dictTypeMapper).insert(argThat(dictType ->
                dictType.name().equals("业务状态")
                        && dictType.code().equals("biz_status")
                        && dictType.description().equals("业务状态字典")
                        && dictType.status().equals(1)
        ));
    }

    @Test
    void createShouldThrowBusinessExceptionWhenCodeExists() {
        DictTypeSaveRequest request = new DictTypeSaveRequest("用户性别", "gender", "用户性别字典", 1);
        when(dictTypeMapper.findByCode("gender")).thenReturn(dictType());

        assertThatThrownBy(() -> dictTypeService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("字典编码已存在");
    }

    @Test
    void updateShouldUpdateDictTypeWhenCodeNotDuplicated() {
        DictType existing = dictType();
        DictTypeSaveRequest request = new DictTypeSaveRequest("用户性别更新", "gender", "更新描述", 0);
        when(dictTypeMapper.findById(1L)).thenReturn(existing);
        when(dictTypeMapper.findByCode("gender")).thenReturn(existing);

        dictTypeService.update(1L, request);

        verify(dictTypeMapper).update(argThat(dictType ->
                dictType.id().equals(1L)
                        && dictType.name().equals("用户性别更新")
                        && dictType.code().equals("gender")
                        && dictType.description().equals("更新描述")
                        && dictType.status().equals(0)
        ));
        verify(dictCacheService).evictItems("gender");
    }

    @Test
    void updateShouldThrowBusinessExceptionWhenCodeBelongsToAnotherType() {
        DictType existing = dictType();
        DictType duplicate = new DictType(2L, "通用状态", "status", "启用禁用状态字典", 1, null, null);
        DictTypeSaveRequest request = new DictTypeSaveRequest("用户性别更新", "status", "更新描述", 1);
        when(dictTypeMapper.findById(1L)).thenReturn(existing);
        when(dictTypeMapper.findByCode("status")).thenReturn(duplicate);

        assertThatThrownBy(() -> dictTypeService.update(1L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("字典编码已存在");
    }

    @Test
    void deleteShouldDeleteDictTypeWhenExists() {
        when(dictTypeMapper.findById(1L)).thenReturn(dictType());
        when(dictTypeMapper.countItemsByTypeId(1L)).thenReturn(0);

        dictTypeService.delete(1L);

        verify(dictTypeMapper).deleteById(1L);
        verify(dictCacheService).evictItems("gender");
    }

    @Test
    void deleteShouldThrowBusinessExceptionWhenDictTypeHasItems() {
        when(dictTypeMapper.findById(1L)).thenReturn(dictType());
        when(dictTypeMapper.countItemsByTypeId(1L)).thenReturn(3);

        assertThatThrownBy(() -> dictTypeService.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("字典类型下存在字典项，不能删除");
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
