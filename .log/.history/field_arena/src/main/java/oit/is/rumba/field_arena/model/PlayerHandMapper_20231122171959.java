package oit.is.rumba.field_arena.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
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

    @Select("select * from playerhand where userName = #{userName} and card_id = #{card_id};")
    ArrayList<PlayerHand> selecthandnum(String userName, int card_id);

    @Delete("delete from playerhand where id = #{id}")
    void deletePlayerHand(int id);
}
