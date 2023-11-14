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
  AsyncFiled_Area asyncFiled_Area;

  Draw player = new Draw();
  HpTest myHp = new HpTest();

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
  public String game(ModelMap model) {
    ArrayList<Card> cards = cardMapper.selectAllCards();
    ArrayList<Card> hand = new ArrayList<Card>();
    this.player = new Draw();
    for (int i = 0; i < 5; i++) {
      hand.add(this.player.getHand(cards));
    }
    this.player.setHandList(hand);
    model.addAttribute("hand", hand);
    return "game.html";
  }

  @GetMapping("/draw")
  public String draw(ModelMap model) {
    ArrayList<Card> cards = cardMapper.selectAllCards();
    ArrayList<Card> hand = new ArrayList<Card>();
    hand = this.player.getHandList();
    hand.add(this.player.getHand(cards));
    this.player.setHandList(hand);
    model.addAttribute("hand", hand);
    return "game.html";
  }

  @GetMapping("/room")
  @Transactional
  public String create_room(@RequestParam String roomName, ModelMap model) {
    asyncFiled_Area.createRoom(roomName);
    model.addAttribute("room", roomName);
    return "room.html";
  }

  @GetMapping("/inroom")
  public String entrRoom(@RequestParam Integer id, ModelMap model) {
    String roomName = roomMapper.selectById(id);
    asyncFiled_Area.enterRoom(id);
    model.addAttribute("room", roomName);
    return "room.html";
  }

  @GetMapping("/active")
  public SseEmitter activeRoom() {
    final SseEmitter emitter = new SseEmitter();
    this.asyncFiled_Area.asyncRoom(emitter);
    return emitter;
  }

  @GetMapping("/hpTest")
  public String hp(Model model) {
    myHp.initHp();
    int hp = myHp.getHp();
    model.addAttribute("hp", hp);
    return "hpTest.html";
  }

  @GetMapping("/attack")
  public String attack(Model model) {
    myHp.minusHp();
    int hp = myHp.getHp();
    model.addAttribute("hp", hp);
    return "hpTest.html";
  }

  @GetMapping("/heal")
  public String heal(Model model) {
    myHp.plusHp();
    int hp = myHp.getHp();
    model.addAttribute("hp", hp);
    return "hpTest.html";
  }

  @GetMapping("/start")
  public SseEmitter start(){
    final SseEmitter emitter = new SseEmitter();
    return emitter;
  }
 

}
