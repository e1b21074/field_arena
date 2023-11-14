package oit.is.rumba.field_arena.model;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RoomMapper {

  @Insert("INSERT INTO rooms (roomName,user1,usernum,isActive) VALUES (#{roomName},#{user1},1,true);")
  void insertName(String roomName);

  @Select("select * from rooms where isActive=true")
  ArrayList<Room> selectByActive();

  @Select("select roomName from rooms where id=#{id}")
  String selectById(int id);

  @Update("UPDATE ROOMS SET USER2=#{user2} USERNUM=2 WHERE id=#{id}")
  void updateById(int id, String user2);

}
