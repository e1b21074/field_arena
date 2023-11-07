package oit.is.rumba.field_arena.controller;

import java.util.ArrayList;
import oit.is.rumba.field_arena.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RuleController {

  @Autowired
  CardMapper cardMapper;

  @GetMapping("/rule") // "/rule"というゲットリクエストがあるとrule.htmlを表示
  public String rule() {
    return "rule.html";
  }

  @GetMapping("/Card")
  public String Card(ModelMap model) {
    ArrayList<Card> cards = cardMapper.selectAllCards();
    model.addAttribute("cards", cards);
    return "rule.html";
  }
}
