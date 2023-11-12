package oit.is.rumba.field_arena.controller;

import java.beans.Transient;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.rumba.field_arena.model.*;

@Controller
public class Field_ArenaController {

  @Autowired
  UserMapper userMapper;

  @Autowired
  CardMapper cardMapper;

  @Autowired
  RoomMapper roomMapper;

  Draw player = new Draw();

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
  public String create_room(@RequestParam String roomName,ModelMap model) {
    roomMapper.insertName(roomName);
    model.addAttribute("room", roomName);
    return "room.html";
  }

}
