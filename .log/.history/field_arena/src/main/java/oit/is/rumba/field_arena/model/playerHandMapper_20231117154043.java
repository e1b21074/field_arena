package oit.is.rumba.field_arena.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface playerHandMapper {
    
    @Select("SELECT * FROM player_hand")
    ArrayList<playerHand> selectAllPlayerHand();

    @Select("SELECT * FROM player_hand WHERE userName = #{userName}")
    ArrayList<playerHand> selectPlayerHandByUserName(String userName);
}
