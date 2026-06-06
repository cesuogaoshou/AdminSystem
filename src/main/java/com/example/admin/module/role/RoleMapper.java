package com.example.admin.module.role;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    Role findById(@Param("id") Long id);

    Role findByCode(@Param("code") String code);

    int countByCode(@Param("code") String code);

    int insert(Role role);

    int update(Role role);

    int deleteById(@Param("id") Long id);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    List<RoleVO> findPage(RoleQueryRequest query);
}
