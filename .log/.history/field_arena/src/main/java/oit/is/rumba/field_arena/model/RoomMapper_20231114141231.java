package oit.is.rumba.field_arena.model;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoomMapper {

  @Insert("INSERT INTO rooms (roomName,isActive) VALUES (#{roomName},true);")
  void insertName(String roomName);

  @Select("select * from rooms where isActive=true")
  ArrayList<Room> selectByActive();

  @Select("select roomName from rooms where id=#{id}")
  String selectById(int id);
}
