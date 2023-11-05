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
  public int drawCard(ModelMap model, Principal prin){
    Random r = new Random();
    int deck[] = {1,2,3};
    int getCard = deck[r.nextInt(3)];
    return getCard;
  }
}
