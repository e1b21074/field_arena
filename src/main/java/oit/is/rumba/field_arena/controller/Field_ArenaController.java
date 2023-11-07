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

}
