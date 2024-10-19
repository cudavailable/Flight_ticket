package com.example.boot_demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.boot_demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
