package com.example.admin.module.log;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogMapper {

    int insert(SysLog sysLog);

    SysLog findById(@Param("id") Long id);

    List<LogVO> findPage(LogQueryRequest query);
}
