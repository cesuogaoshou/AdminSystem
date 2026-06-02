package com.example.admin.module.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    User findById(@Param("id") Long id);

    User findByUsername(@Param("username") String username);

    int countByUsername(@Param("username") String username);

    List<UserVO> findPage(UserQueryRequest query);
}