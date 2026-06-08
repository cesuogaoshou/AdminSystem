package com.example.admin.module.log;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LogMapper {

    int insert(SysLog sysLog);

    SysLog findById(@Param("id") Long id);
}
