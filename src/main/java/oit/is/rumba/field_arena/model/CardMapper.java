package oit.is.rumba.field_arena.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CardMapper {

  @Select("select * from cards")
  ArrayList<Card> selectAllCards();

  @Select("select * from cards where id=#{id}")
  Card selectCardById(int id);

}
