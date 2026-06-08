package com.example.admin.module.dict;

import com.example.admin.common.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictItemService {

    private final DictItemMapper dictItemMapper;
    private final DictTypeMapper dictTypeMapper;
    private final DictCacheService dictCacheService;

    public DictItemService(
            DictItemMapper dictItemMapper,
            DictTypeMapper dictTypeMapper,
            DictCacheService dictCacheService
    ) {
        this.dictItemMapper = dictItemMapper;
        this.dictTypeMapper = dictTypeMapper;
        this.dictCacheService = dictCacheService;
    }

    public List<DictItem> listByTypeId(Long typeId) {
        ensureTypeExists(typeId);
        return dictItemMapper.findByTypeId(typeId);
    }

    public List<DictItem> listByTypeCode(String typeCode) {
        List<DictItem> cachedItems = dictCacheService.getItems(typeCode);
        if (cachedItems != null) {
            return cachedItems;
        }

        if (dictTypeMapper.findByCode(typeCode) == null) {
            throw new BusinessException(404, "字典类型不存在");
        }

        List<DictItem> items = dictItemMapper.findByTypeCode(typeCode);
        dictCacheService.putItems(typeCode, items);
        return items;
    }

    public void create(Long typeId, DictItemSaveRequest request) {
        ensureTypeExists(typeId);
        if (dictItemMapper.findByTypeIdAndValue(typeId, request.value()) != null) {
            throw new BusinessException(400, "字典项值已存在");
        }

        DictItem dictItem = new DictItem(
                null,
                typeId,
                request.label(),
                request.value(),
                request.color(),
                request.sortOrder() == null ? 0 : request.sortOrder(),
                request.status() == null ? 1 : request.status(),
                null,
                null
        );

        dictItemMapper.insert(dictItem);
        evictTypeItems(typeId);
    }

    public void update(Long id, DictItemSaveRequest request) {
        DictItem existing = dictItemMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "字典项不存在");
        }

        DictItem duplicate = dictItemMapper.findByTypeIdAndValue(existing.typeId(), request.value());
        if (duplicate != null && !duplicate.id().equals(id)) {
            throw new BusinessException(400, "字典项值已存在");
        }

        DictItem dictItem = new DictItem(
                existing.id(),
                existing.typeId(),
                request.label(),
                request.value(),
                request.color(),
                request.sortOrder() == null ? existing.sortOrder() : request.sortOrder(),
                request.status() == null ? existing.status() : request.status(),
                existing.createTime(),
                existing.updateTime()
        );

        dictItemMapper.update(dictItem);
        evictTypeItems(existing.typeId());
    }

    public void delete(Long id) {
        DictItem existing = dictItemMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "字典项不存在");
        }
        dictItemMapper.deleteById(id);
        evictTypeItems(existing.typeId());
    }

    private void ensureTypeExists(Long typeId) {
        if (dictTypeMapper.findById(typeId) == null) {
            throw new BusinessException(404, "字典类型不存在");
        }
    }

    private void evictTypeItems(Long typeId) {
        DictType dictType = dictTypeMapper.findById(typeId);
        if (dictType != null) {
            dictCacheService.evictItems(dictType.code());
        }
    }
}
