package oit.is.rumba.field_arena.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RoomMapper {

  @Insert("INSERT INTO rooms (roomName,user1,usernum,isActive) VALUES (#{roomName},#{user1},1,true);")
  void insertName(String roomName, String user1);

  @Select("select * from rooms where isActive=true")
  ArrayList<Room> selectByActive();

  @Select("select roomName from rooms where id=#{id}")
  String selectById(int id);

  @Update("UPDATE ROOMS SET user2=#{user2}, USERNUM=2 WHERE id=#{id}")
  void updateById(int id, String user2);

  @Select("select * from rooms where isActive=true and usernum=2")
  Room selectAllByAtiveandNum();

  @Select("select * from rooms where user1=#{userName} and isActive=true")
  int selectIdByUser1Name(String userName);

  @Select("select * from rooms where roomName=#{roomName} and isActive=true")
  Room checkByroomName(String roomName);

  @Update("UPDATE ROOMS SET isActive=false where id=#{id}")
  void updateActiveById(int id);

}
