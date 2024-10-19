package com.example.boot_demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.boot_demo.entity.Admin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper extends BaseMapper<Admin>{

//    @Select("select * from admin")
//    public List<Admin> find();
//
//    @Insert("insert into admin values(#{id}, #{account}, #{pwd})")
//    public int insert(Admin admin);

}
