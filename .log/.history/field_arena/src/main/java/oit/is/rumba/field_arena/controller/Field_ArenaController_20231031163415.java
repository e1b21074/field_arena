package oit.is.rumba.field_arena.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Field_ArenaController {

    @GetMapping("/gamearea")
  public String gamearea() {
    return "gamearea.html";
  }
  
}
