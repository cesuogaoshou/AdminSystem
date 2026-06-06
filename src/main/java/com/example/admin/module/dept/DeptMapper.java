package com.example.admin.module.dept;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptMapper {

    List<Dept> findAll();

    Dept findById(@Param("id") Long id);
}