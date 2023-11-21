package oit.is.rumba.field_arena.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.ui.Model;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.rumba.field_arena.model.*;
import oit.is.rumba.field_arena.service.*;

@Controller
public class Field_ArenaController {

  @Autowired
  UserMapper userMapper;

  @Autowired
  CardMapper cardMapper;

  @Autowired
  RoomMapper roomMapper;

  @Autowired
  HpMapper hpMapper;

  @Autowired
  PlayerHandMapper playerHandMapper;

  @Autowired
  AsyncFiled_Area asyncFiled_Area;

  @GetMapping("/gamearea")
  public String gamearea() {
    return "gamearea.html";
  }

  @GetMapping("logininfo")
  public String loginInfo(ModelMap model, Principal prin) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);
    return "gamearea.html";
  }

  @GetMapping("/User")
  public String User(ModelMap model) {
    ArrayList<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);
    return "gamearea.html";
  }

  @GetMapping("/game")
  public String game(@RequestParam Integer id, ModelMap model, Principal prin) {
    ArrayList<Card> cards = cardMapper.selectAllCards();
    PlayerHand hand = new PlayerHand();
    Draw player = new Draw();
    for (int i = 0; i < 5; i++) {
      hand.setUserName(prin.getName());
      hand.setCard_id(player.getHand(cards).getId());
      playerHandMapper.setPlayerHand(hand.getUserName(), hand.getCard_id());
    }
    model.addAttribute("playerhand", playerHandMapper.selectCardByUserName(prin.getName()));

    // HP処理
    int roomsId = id;// 一旦定数->rommMapperを使用して受け取りたい ←部屋のidをリンクのパラムとして取得した
    String userName = prin.getName();
    // hpMapper.createHp(roomsId, userName);
    Hp hp = hpMapper.selectMyHp(roomsId, userName);
    model.addAttribute("hp", hp.getHp());
    model.addAttribute("roomsId", roomsId);
    // 敵のHP
    Hp enemyHp = hpMapper.selectEnemyHp(roomsId, userName);
    model.addAttribute("enemy", enemyHp);
    return "game.html";
  }

  @GetMapping("/draw")
  public String draw(@RequestParam String id, ModelMap model, Principal prin) {
    ArrayList<Card> cards = cardMapper.selectAllCards();
    PlayerHand hand = new PlayerHand();
    Draw player = new Draw();
    hand.setUserName(prin.getName());
    hand.setCard_id(player.getHand(cards).getId());
    playerHandMapper.setPlayerHand(hand.getUserName(), hand.getCard_id());
    model.addAttribute("playerhand", playerHandMapper.selectCardByUserName(prin.getName()));

    // HP
    int roomsId = Integer.parseInt(id);// 一旦定数->rommMapperを使用して受け取りたい
    String userName = prin.getName();
    Hp hp = hpMapper.selectMyHp(roomsId, userName);
    model.addAttribute("hp", hp.getHp());
    model.addAttribute("roomsId", roomsId);
    // 敵のHP
    Hp enemyHp = hpMapper.selectEnemyHp(roomsId, userName);
    model.addAttribute("enemy", enemyHp);
    return "game.html";
  }

  @GetMapping("/room")
  @Transactional
  public String create_room(@RequestParam String roomName, Principal prin, ModelMap model) {
    asyncFiled_Area.createRoom(roomName, prin.getName());
    model.addAttribute("room", roomName);
    String userName = prin.getName();
    int roomId = roomMapper.selectIdByUser1Name(userName);
    hpMapper.createHp(roomId, userName);
    return "room.html";
  }

  @GetMapping("/inroom")
  public String entrRoom(@RequestParam Integer id, Principal prin, ModelMap model) {
    String roomName = roomMapper.selectById(id);
    asyncFiled_Area.enterRoom(id, prin.getName());
    model.addAttribute("room", roomName);
    String userName = prin.getName();
    int roomId = id;
    hpMapper.createHp(roomId, userName);
    return "room.html";
  }

  @GetMapping("/active")
  public SseEmitter activeRoom() {
    final SseEmitter emitter = new SseEmitter();
    this.asyncFiled_Area.asyncRoom(emitter);
    return emitter;
  }

  /*
   * @GetMapping("/hpTest")
   * public String hp(Model model) {
   * myHp.initHp();
   * int hp = myHp.getHp();
   * model.addAttribute("hp", hp);
   * return "hpTest.html";
   * }
   */

  @GetMapping("/cardUse")
  public String cardUse(@RequestParam String id, Model model, Principal prin) {
    Card card = cardMapper.selectCardById(Integer.parseInt(id));

    if (card.getCardAttribute().equals("武器")) {
      attack(model, prin);
    } else if (card.getCardAttribute().equals("防具")) {

    } else if (card.getCardAttribute().equals("回復")) {
      heal(model, prin);
    }

    return "game.html";
  }

  // @GetMapping("/attack")
  public String attack(Model model, Principal prin) {
    // 敵のHP
    int roomsId = 1;// Integer.parseInt(id);// 一旦定数->rommMapperを使用して受け取りたい
    String userName = prin.getName();
    Hp enemyHp = hpMapper.selectEnemyHp(roomsId, userName);// 同じルームの自分の名前じゃないプレイヤーのHP
    enemyHp.minusHp();
    hpMapper.updateEnemyHp(roomsId, userName, enemyHp.getHp());
    model.addAttribute("enemy", enemyHp);
    // 自分のHP
    Hp myHp = hpMapper.selectMyHp(roomsId, userName);
    model.addAttribute("hp", myHp.getHp());
    model.addAttribute("playerhand", playerHandMapper.selectCardByUserName(prin.getName()));
    model.addAttribute("roomsId", roomsId);
    return "game.html";
  }

  // @GetMapping("/heal")
  public String heal(Model model, Principal prin) {
    int roomsId = 1;// Integer.parseInt(id);// 一旦定数->rommMapperを使用して受け取りたい
    String userName = prin.getName();
    Hp myHp = hpMapper.selectMyHp(roomsId, userName);
    myHp.plusHp();
    hpMapper.updateMyHp(roomsId, userName, myHp.getHp());
    model.addAttribute("hp", myHp.getHp());
    model.addAttribute("playerhand", playerHandMapper.selectCardByUserName(prin.getName()));
    // 敵のHP
    Hp enemyHp = hpMapper.selectEnemyHp(roomsId, userName);
    model.addAttribute("enemy", enemyHp);
    return "game.html";
  }

  @GetMapping("/start")
  public SseEmitter start() {
    final SseEmitter emitter = new SseEmitter();
    this.asyncFiled_Area.asyncEnter(emitter);
    return emitter;
  }

}
