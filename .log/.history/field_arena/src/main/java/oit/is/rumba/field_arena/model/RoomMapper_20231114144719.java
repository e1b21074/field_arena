package oit.is.rumba.field_arena.model;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RoomMapper {

  @Insert("INSERT INTO rooms (roomName,usernum,isActive) VALUES (#{roomName},1,true);")
  void insertName(String roomName);

  @Select("select * from rooms where isActive=true")
  ArrayList<Room> selectByActive();

  @Select("select roomName from rooms where id=#{id}")
  String selectById(int id);

  @Update("UPDATE ROOMS SET USERNUM=2 WHERE id=#{id}")
  void updateById(int id);

}
