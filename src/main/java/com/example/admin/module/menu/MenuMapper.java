package com.example.admin.module.menu;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {

    List<Menu> findAll();

    Menu findById(@Param("id") Long id);

    int insert(Menu menu);

    int update(Menu menu);

    int countByParentId(@Param("parentId") Long parentId);

    int countRolesByMenuId(@Param("menuId") Long menuId);

    int deleteById(@Param("id") Long id);
}
