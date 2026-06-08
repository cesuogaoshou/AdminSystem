package com.example.admin.module.dict;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictTypeMapper {

    List<DictType> findAll();

    DictType findById(@Param("id") Long id);

    DictType findByCode(@Param("code") String code);

    int insert(DictType dictType);

    int update(DictType dictType);

    int deleteById(@Param("id") Long id);
}
