package oit.is.rumba.field_arena.controller;

import java.security.Principal;

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
  public String sample32(ModelMap model, Principal prin) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);
    return "gamearea.html";
  }
}
