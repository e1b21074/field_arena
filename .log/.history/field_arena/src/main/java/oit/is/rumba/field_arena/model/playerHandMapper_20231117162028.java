package oit.is.rumba.field_arena.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface playerHandMapper {
    
    @Select("SELECT * FROM player_hand")
    ArrayList<playerHand> selectAllPlayerHand();

    @Select("SELECT * FROM player_hand WHERE userName = #{userName}")
    ArrayList<playerHand> selectPlayerHandByUserName(String userName);

    @Insert("INSERT INTO playerHand (userName,card_id) VALUES (#{user},#{card_id});")
    void setPlayerHand(String user, int card_id);

    @Select("SELECT cards.id, cardAttribute, cardStrong FROM player_hand join cards WHERE userName = #{userName}")
    ArrayList<Card> selectCardByUserName(String userName);
}
