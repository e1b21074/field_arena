package oit.is.rumba.field_arena.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PlayerHandMapper {
    
    @Select("SELECT * FROM player_hand;")
    ArrayList<PlayerHand> selectAllPlayerHand();

    @Insert("INSERT INTO playerHand (userName,card_id) VALUES (#{user},#{card_id});")
    void setPlayerHand(String user, int card_id);

    @Select("SELECT cards.id, cardAttribute, cardStrong FROM  playerhand join cards on (card_id = cards.id) WHERE userName = #{userName};")
    ArrayList<Card> selectCardByUserName(String userName);
}
