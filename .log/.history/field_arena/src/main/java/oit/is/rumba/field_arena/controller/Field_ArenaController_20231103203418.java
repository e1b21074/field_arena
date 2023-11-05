package oit.is.rumba.field_arena.controller;

import java.security.Principal;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Field_ArenaController {

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

  @GetMapping("drawcard")
  public String drawCard(ModelMap model){
    Random r = new Random();
    String deck[] = {"攻撃1","攻撃2","攻撃3","防御1","防御2","防御3"};
    String getCard = deck[r.nextInt(6)];
    model.addAttribute("getCard", getCard);
    return "gamearea.html";
  }
}
