package oit.is.rumba.field_arena.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;



@Mapper
public interface UserMapper {

  @Select("select * from userinfo")
  ArrayList<User> selectAllUsers();

  @Update("update userinfo set isActive = true where userName=#{userName}")
  void updateActiveToTrue(String userName);

  @Update("update userinfo set isActive = false where userName=#{userName}")
  void updateActiveTofalse(String userName);

  @Select("select * from userinfo where userName = #{userName}")
  User selectUserByName(String userName);

}
