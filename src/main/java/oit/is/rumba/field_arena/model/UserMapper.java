package oit.is.rumba.field_arena.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;



@Mapper
public interface UserMapper {

  @Select("select * from userinfo")
  ArrayList<User> selectAllUsers();
  
}
