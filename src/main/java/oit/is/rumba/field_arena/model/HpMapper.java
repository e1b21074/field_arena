package oit.is.rumba.field_arena.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface HpMapper {

  @Select("select * from userHp where roomsId=#{roomsId} and userName=#{userName}")
  Hp selectMyHp(int roomsId, String userName);

  @Select("select * from userHp where roomsId=#{roomsId} and userName <> #{userName}")
  Hp selectEnemyHp(int roomsId, String userName);

  @Insert("INSERT INTO userHp (roomsId,userName,hp,attackFlag) VALUES (#{roomsId},#{user},10,false);")
  void createHp(int roomsId, String user);

  @Update("UPDATE userHp SET hp=#{hp} WHERE roomsId = #{roomsId} AND userName = #{userName}")
  void updateMyHp(int roomsId, String userName, int hp);

  @Update("UPDATE userHp SET hp=#{hp} WHERE roomsId = #{roomsId} AND userName <> #{userName}")
  void updateEnemyHp(int roomsId, String userName, int hp);

  @Select("select * from userHp where roomsid=#{roomid}")
  ArrayList<Hp> selectByRoomId(int roomid);

  @Select("select * from userHp where roomsid=#{roomid} and userName <> #{userName}")
  Hp selectByroomIdAndUsername(int roomid, String userName);

  @Update("UPDATE userHp SET attackFlag=true,attackPoint=#{attackPoint} WHERE roomsId = #{roomsId} AND userName <> #{userName}")
  void updateAttackTrue(int roomsId, String userName,int attackPoint);

  @Update("UPDATE userHp SET attackFlag=false,attackPoint=#{attackPoint} WHERE roomsId = #{roomsId} AND userName = #{userName}")
  void updateAttackFalse(int roomsId, String userName,int attackPoint);

  @Select("select attackFlag from userHp where roomsId = #{roomsId} AND userName = #{userName}")
  int selectFlag(int roomsId, String userName);

}
