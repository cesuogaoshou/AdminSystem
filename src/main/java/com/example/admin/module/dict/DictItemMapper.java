package com.example.admin.module.dict;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictItemMapper {

    List<DictItem> findByTypeId(@Param("typeId") Long typeId);

    DictItem findById(@Param("id") Long id);

    DictItem findByTypeIdAndValue(@Param("typeId") Long typeId, @Param("value") String value);

    int insert(DictItem dictItem);

    int update(DictItem dictItem);

    int deleteById(@Param("id") Long id);
}
