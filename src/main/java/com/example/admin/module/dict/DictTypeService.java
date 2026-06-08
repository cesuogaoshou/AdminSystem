package com.example.admin.module.dict;

import com.example.admin.common.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictTypeService {

    private final DictTypeMapper dictTypeMapper;

    public DictTypeService(DictTypeMapper dictTypeMapper) {
        this.dictTypeMapper = dictTypeMapper;
    }

    public List<DictType> list() {
        return dictTypeMapper.findAll();
    }

    public DictType getById(Long id) {
        DictType dictType = dictTypeMapper.findById(id);
        if (dictType == null) {
            throw new BusinessException(404, "字典类型不存在");
        }
        return dictType;
    }

    public void create(DictTypeSaveRequest request) {
        if (dictTypeMapper.findByCode(request.code()) != null) {
            throw new BusinessException(400, "字典编码已存在");
        }

        DictType dictType = new DictType(
                null,
                request.name(),
                request.code(),
                request.description(),
                request.status() == null ? 1 : request.status(),
                null,
                null
        );

        dictTypeMapper.insert(dictType);
    }

    public void update(Long id, DictTypeSaveRequest request) {
        DictType existing = getById(id);
        DictType duplicate = dictTypeMapper.findByCode(request.code());
        if (duplicate != null && !duplicate.id().equals(id)) {
            throw new BusinessException(400, "字典编码已存在");
        }

        DictType dictType = new DictType(
                existing.id(),
                request.name(),
                request.code(),
                request.description(),
                request.status() == null ? existing.status() : request.status(),
                existing.createTime(),
                existing.updateTime()
        );

        dictTypeMapper.update(dictType);
    }

    public void delete(Long id) {
        getById(id);
        if (dictTypeMapper.countItemsByTypeId(id) > 0) {
            throw new BusinessException(400, "字典类型下存在字典项，不能删除");
        }
        dictTypeMapper.deleteById(id);
    }
}
