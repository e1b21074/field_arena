package oit.is.rumba.field_arena.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;

@Controller
public class Field_ArenaController {

    @GetMapping("/gamearea")
  public String gamearea() {
    return "gamearea.html";
  }

 @GetMapping("logininfo")
  public String sample32(ModelMap model, Principal prin) {
    String loginUser = prin.getName(); // ログインユーザ情報
    model.addAttribute("loginuser", loginUser);
    return "gamearea.html";
  }
}
