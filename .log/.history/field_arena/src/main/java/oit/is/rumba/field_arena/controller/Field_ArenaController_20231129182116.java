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
  public String game(@RequestParam Integer roomid, ModelMap model, Principal prin) {
    ArrayList<Card> cards = cardMapper.selectAllCards();
    PlayerHand hand = new PlayerHand();
    Draw player = new Draw();
    for (int i = 0; i < 5; i++) {
      hand.setUserName(prin.getName());
      hand.setCard_id(player.getHand(cards).getId());
      playerHandMapper.setPlayerHand(hand.getUserName(), hand.getCard_id());
    }
    model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));

    // HP処理
    int roomsId = roomid;// 一旦定数->rommMapperを使用して受け取りたい ←部屋のidをリンクのパラムとして取得した
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
  public String draw(@RequestParam String roomid, ModelMap model, Principal prin) {
    int roomsId = Integer.parseInt(roomid);// 一旦定数->rommMapperを使用して受け取りたい
    String userName = prin.getName();
    ArrayList<Card> cards = cardMapper.selectAllCards();
    PlayerHand hand = new PlayerHand();
    Draw player = new Draw();
    int handnum = playerHandMapper.selectCardByUserName(userName).size();
    
    if(prin.getName().equals(roomMapper.selectTurnsById(roomsId))){
      if(handnum >= 10){
        playerHandMapper.deletePlayerHand(playerHandMapper.selecthandByUserName(userName).get(0).getId());
      }
      hand.setUserName(prin.getName());
      hand.setCard_id(player.getHand(cards).getId());
      playerHandMapper.setPlayerHand(hand.getUserName(), hand.getCard_id());
      Hp Hps = hpMapper.selectByroomIdAndUsername(roomsId, userName);
      roomMapper.changeTurns(roomsId, Hps.getUserName());
    }
    model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));

    // HP
    Hp hp = hpMapper.selectMyHp(roomsId, userName);
    model.addAttribute("hp", hp.getHp());
    model.addAttribute("roomsId", roomsId);
    // 敵のHP
    Hp enemyHp = hpMapper.selectEnemyHp(roomsId, userName);
    model.addAttribute("enemy", enemyHp);
    model.addAttribute("turns", roomMapper.selectTurnsById(roomsId));
    return "game.html";
  }

  @GetMapping("/room")
  @Transactional
  public String create_room(@RequestParam String roomName, Principal prin, ModelMap model) {
    if (roomMapper.checkByroomName(roomName) != null) {
      model.addAttribute("AlreadyRoom", roomName);
      return "gamearea.html";
    }
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

  // @GetMapping("/hpTest")
  // public String hp(Model model) {
  // myHp.initHp();
  // int hp = myHp.getHp();
  // model.addAttribute("hp", hp);
  // return "hpTest.html";
  // }

  @GetMapping("/cardUse")
  public String cardUse(@RequestParam String id, @RequestParam Integer roomid, Model model, Principal prin) {
    Card card = cardMapper.selectCardById(Integer.parseInt(id));
    Hp Hps = hpMapper.selectByroomIdAndUsername(roomid, prin.getName());

    if (card.getCardAttribute().equals("武器") && prin.getName().equals(roomMapper.selectTurnsById(roomid))) {
      attack(card, roomid, model, prin);
      roomMapper.changeTurns(roomid, Hps.getUserName());
    } else if (card.getCardAttribute().equals("防具") && !prin.getName().equals(roomMapper.selectTurnsById(roomid))) {
      block(card, roomid, model, prin);
    } else if (card.getCardAttribute().equals("回復") && prin.getName().equals(roomMapper.selectTurnsById(roomid))) {
      heal(card, roomid, model, prin);
      roomMapper.changeTurns(roomid, Hps.getUserName());
    }else{
      int roomsId = roomid;
      String userName = prin.getName();
      Hp myHp = hpMapper.selectMyHp(roomsId, userName);
      model.addAttribute("hp", myHp.getHp());
      model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));
      Hp enemyHp = hpMapper.selectEnemyHp(roomsId, userName);
      model.addAttribute("enemy", enemyHp);
      model.addAttribute("roomsId", roomsId);
    }
    model.addAttribute("turns", roomMapper.selectTurnsById(roomid));
    return "game.html";
  }

  @GetMapping("/attack")
  public String attack(Card card, int roomid, Model model, Principal prin) {
    // 敵のHP
    int roomsId = roomid;// Integer.parseInt(id);// 一旦定数->rommMapperを使用して受け取りたい
    String userName = prin.getName();
    Hp enemyHp = hpMapper.selectEnemyHp(roomsId, userName);// 同じルームの自分の名前じゃないプレイヤーのHP
    enemyHp.minusHp(card.getCardStrong());
    hpMapper.updateEnemyHp(roomsId, userName, enemyHp.getHp());
    model.addAttribute("enemy", enemyHp);
    // 自分のHP
    Hp myHp = hpMapper.selectMyHp(roomsId, userName);
    model.addAttribute("hp", myHp.getHp());
    playerHandMapper.deletePlayerHand(playerHandMapper.selecthandnum(userName, card.getId()).get(0).getId());
    model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));
    model.addAttribute("roomsId", roomsId);
    return "game.html";
  }

  @GetMapping("/heal")
  public String heal(Card card, int roomid, Model model, Principal prin) {
    int roomsId = roomid;// Integer.parseInt(id);// 一旦定数->rommMapperを使用して受け取りたい
    String userName = prin.getName();
    Hp myHp = hpMapper.selectMyHp(roomsId, userName);
    myHp.plusHp(card.getCardStrong());
    hpMapper.updateMyHp(roomsId, userName, myHp.getHp());
    model.addAttribute("hp", myHp.getHp());
    playerHandMapper.deletePlayerHand(playerHandMapper.selecthandnum(userName, card.getId()).get(0).getId());
    model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));
    // 敵のHP
    Hp enemyHp = hpMapper.selectEnemyHp(roomsId, userName);
    model.addAttribute("enemy", enemyHp);
    model.addAttribute("roomsId", roomsId);
    return "game.html";
  }

  @GetMapping("/block")
  public String block(Card card, int roomid, Model model, Principal prin) {
    int roomsId = roomid;
    String userName = prin.getName();
    Hp myHp = hpMapper.selectMyHp(roomsId, userName);
    model.addAttribute("hp", myHp.getHp());
    // 選択されたカードを削除する
    playerHandMapper.deletePlayerHand(playerHandMapper.selecthandnum(userName, card.getId()).get(0).getId());
    model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));
    Hp enemyHp = hpMapper.selectEnemyHp(roomsId, userName);
    model.addAttribute("enemy", enemyHp);
    model.addAttribute("roomsId", roomsId);

    return "game.html";
  }

  @GetMapping("/start")
  public SseEmitter start() {
    final SseEmitter emitter = new SseEmitter();
    this.asyncFiled_Area.asyncEnter(emitter);
    return emitter;
  }

  @GetMapping("/HPasync")
  public SseEmitter HPasync(@RequestParam Integer roomid, Principal prin) {
    final SseEmitter emitter = new SseEmitter();
    this.asyncFiled_Area.HPAsyncEmitter(emitter, roomid, prin.getName());
    return emitter;
  }

  private ArrayList<Card> sort(ArrayList<Card> hand) {
    for (int i = 0; i < hand.size(); i++) {
      for (int j = 0; j < hand.size(); j++) {
        if (hand.get(i).getId() < hand.get(j).getId()) {
          Card tmp = hand.get(i);
          hand.set(i, hand.get(j));
          hand.set(j, tmp);
        }
      }
    }
    return hand;
  }

}
