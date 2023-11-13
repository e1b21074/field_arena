package oit.is.rumba.field_arena.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import oit.is.rumba.field_arena.model.*;

@Controller
public class Field_ArenaController {

  @Autowired
  UserMapper userMapper;

  @Autowired
  CardMapper cardMapper;

  Draw player = new Draw();
  Draw Cpu = new Draw();

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
    ArrayList<Card> playerHand = new ArrayList<Card>();
    ArrayList<Card> cpuHand = new ArrayList<Card>();
    for (int i = 0; i < 5; i++) {
      playerHand.add(this.player.getHand(cards));
      cpuHand.add(this.Cpu.getHand(cards));
    }
    this.player.setHandList(playerHand);
    this.Cpu.setHandList(cpuHand);
    model.addAttribute("playerhand", playerHand);
    model.addAttribute("cpuhand", cpuHand);
    return "game.html";
  }

  @GetMapping("/draw")
  public String draw(ModelMap model) {
    ArrayList<Card> cards = cardMapper.selectAllCards();
    ArrayList<Card> hand = new ArrayList<Card>();
    ArrayList<Card> chand = new ArrayList<>();
    hand = this.player.getHandList();
    hand.add(this.player.getHand(cards));
    this.player.setHandList(hand);
    model.addAttribute("hand", hand);
    return "game.html";
  }

}
